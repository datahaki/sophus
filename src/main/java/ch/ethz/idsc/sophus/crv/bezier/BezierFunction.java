// code by jph
package ch.ethz.idsc.sophus.crv.bezier;

import java.util.Objects;

import ch.ethz.idsc.sophus.hs.BiinvariantMean;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.api.ScalarTensorFunction;
import ch.ethz.idsc.tensor.ext.Integers;
import ch.ethz.idsc.tensor.itp.BernsteinBasis;
import ch.ethz.idsc.tensor.itp.BinaryAverage;

/** <p>For parameters in the unit interval [0, 1] the function gives values "in between" the
 * control points.
 * 
 * <p>BezierFunction can be used for extrapolation when using parameters outside the unit
 * interval.
 * 
 * <p>Reference:
 * <a href="https://en.wikipedia.org/wiki/De_Casteljau%27s_algorithm">De Casteljau's algorithm</a>
 * 
 * <p>inspired by
 * <a href="https://reference.wolfram.com/language/ref/BezierFunction.html">BezierFunction</a>
 * 
 * @see BernsteinBasis */
public class BezierFunction implements ScalarTensorFunction {
  /** De Casteljau's algorithm for the evaluation of Bezier curves, i.e. the splitting operation
   * is between two elements (not a weighted average between many). The binary split is provided
   * via the interface {@link BinaryAverage}.
   *
   * @param binaryAverage
   * @param sequence non-empty tensor
   * @return function over the interval [0, 1]
   * @throws Exception if given sequence is empty or a scalar */
  public static ScalarTensorFunction of(BinaryAverage binaryAverage, Tensor sequence) {
    Integers.requirePositive(sequence.length());
    return new BezierFunction(Objects.requireNonNull(binaryAverage), sequence);
  }

  /** evaluation using biinvariantMean with weights from Bernstein polynomials
   * 
   * @param biinvariantMean
   * @param sequence non-empty tensor
   * @return */
  public static ScalarTensorFunction of(BiinvariantMean biinvariantMean, Tensor sequence) {
    Objects.requireNonNull(biinvariantMean);
    int degree = Integers.requirePositiveOrZero(sequence.length() - 1);
    return scalar -> biinvariantMean.mean(sequence, BernsteinBasis.of(degree, scalar));
  }

  /***************************************************/
  private final BinaryAverage binaryAverage;
  private final Tensor sequence;

  private BezierFunction(BinaryAverage binaryAverage, Tensor sequence) {
    this.binaryAverage = binaryAverage;
    this.sequence = sequence;
  }

  @Override // from ScalarTensorFunction
  public Tensor apply(Scalar scalar) {
    Tensor[] points = sequence.stream().toArray(Tensor[]::new);
    for (int i = points.length; 1 <= i; --i) {
      int count = -1;
      Tensor p = points[0];
      for (int index = 1; index < i; ++index)
        points[++count] = binaryAverage.split(p, p = points[index], scalar);
    }
    return points[0];
  }
}

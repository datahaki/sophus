// original code by Behzad Torkian
// adapted to geodesic averages by jph
package ch.ethz.idsc.sophus.crv.spline;

import java.util.Objects;

import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.VectorQ;
import ch.ethz.idsc.tensor.opt.BinaryAverage;
import ch.ethz.idsc.tensor.opt.ScalarTensorFunction;

/** Neville's algorithm for polynomial interpolation by Eric Harold Neville
 * 
 * https://en.wikipedia.org/wiki/Neville%27s_algorithm */
public class GeodesicNeville implements ScalarTensorFunction {
  /** @param binaryAverage
   * @param knots vector
   * @param tensor
   * @return */
  public static ScalarTensorFunction of(BinaryAverage binaryAverage, Tensor knots, Tensor tensor) {
    return new GeodesicNeville( //
        Objects.requireNonNull(binaryAverage), //
        VectorQ.requireLength(knots, tensor.length()), //
        tensor);
  }

  /***************************************************/
  private final BinaryAverage binaryAverage;
  private final Scalar[] knots;
  private final Tensor tensor;

  private GeodesicNeville(BinaryAverage binaryAverage, Tensor knots, Tensor tensor) {
    this.binaryAverage = binaryAverage;
    this.knots = knots.stream().map(Scalar.class::cast).toArray(Scalar[]::new);
    this.tensor = tensor;
  }

  @Override // from ScalarTensorFunction
  public Tensor apply(Scalar scalar) {
    int length = knots.length;
    Tensor[] d = tensor.stream().toArray(Tensor[]::new);
    for (int j = 1; j < length; ++j)
      for (int i = length - 1; j <= i; --i) {
        Scalar ratio = knots[i].subtract(scalar).divide(knots[i].subtract(knots[i - j]));
        d[i] = binaryAverage.split(d[i], d[i - 1], ratio);
      }
    return d[length - 1];
  }
}

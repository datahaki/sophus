// code by jph
package ch.ethz.idsc.sophus.crv.spline;

import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.Range;
import ch.ethz.idsc.tensor.alg.VectorQ;
import ch.ethz.idsc.tensor.opt.AbstractInterpolation;
import ch.ethz.idsc.tensor.opt.BinaryAverage;
import ch.ethz.idsc.tensor.opt.InterpolatingPolynomial;
import ch.ethz.idsc.tensor.opt.Interpolation;
import ch.ethz.idsc.tensor.opt.ScalarTensorFunction;

/** implementation uses knots 0, 1, 2, ...
 * 
 * @see InterpolatingPolynomial */
public class LagrangeInterpolation extends AbstractInterpolation {
  /** @param binaryAverage
   * @param tensor
   * @return */
  public static Interpolation of(BinaryAverage binaryAverage, Tensor tensor) {
    return new LagrangeInterpolation(binaryAverage, tensor);
  }

  /***************************************************/
  private final ScalarTensorFunction scalarTensorFunction;

  private LagrangeInterpolation(BinaryAverage binaryAverage, Tensor tensor) {
    scalarTensorFunction = InterpolatingPolynomial.of(binaryAverage, Range.of(0, tensor.length())) //
        .scalarTensorFunction(tensor);
  }

  @Override // from Interpolation
  public Tensor get(Tensor index) {
    return at(VectorQ.requireLength(index, 1).Get(0));
  }

  @Override // from Interpolation
  public Tensor at(Scalar index) {
    return scalarTensorFunction.apply(index);
  }
}

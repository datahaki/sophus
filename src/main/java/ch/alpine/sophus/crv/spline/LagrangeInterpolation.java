// code by jph
package ch.alpine.sophus.crv.spline;

import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Range;
import ch.alpine.tensor.alg.VectorQ;
import ch.alpine.tensor.api.ScalarTensorFunction;
import ch.alpine.tensor.itp.AbstractInterpolation;
import ch.alpine.tensor.itp.BinaryAverage;
import ch.alpine.tensor.itp.InterpolatingPolynomial;
import ch.alpine.tensor.itp.Interpolation;

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

  // ---
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

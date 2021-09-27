// code by jph
package ch.alpine.sophus.itp;

import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.TensorRuntimeException;
import ch.alpine.tensor.api.ScalarTensorFunction;
import ch.alpine.tensor.itp.BinaryAverage;
import ch.alpine.tensor.itp.Interpolation;
import ch.alpine.tensor.pdf.EqualizingDistribution;
import ch.alpine.tensor.pdf.InverseCDF;

/** function defined on the interval [0, 1] */
public class ArcLengthParameterization implements ScalarTensorFunction {
  /** @param distances vector with non-negative entries
   * @param binaryAverage non-null
   * @param tensor with length one more than length of given differences
   * @return */
  public static ScalarTensorFunction of(Tensor distances, BinaryAverage binaryAverage, Tensor tensor) {
    if (distances.length() + 1 == tensor.length())
      return new ArcLengthParameterization(distances, binaryAverage, tensor);
    throw TensorRuntimeException.of(distances, tensor);
  }

  // ---
  private final InverseCDF inverseCDF;
  private final Interpolation interpolation;

  private ArcLengthParameterization(Tensor distances, BinaryAverage binaryAverage, Tensor tensor) {
    inverseCDF = (InverseCDF) EqualizingDistribution.fromUnscaledPDF(distances);
    interpolation = GeodesicInterpolation.of(binaryAverage, tensor);
  }

  @Override
  public Tensor apply(Scalar scalar) {
    return interpolation.at(inverseCDF.quantile(scalar));
  }
}

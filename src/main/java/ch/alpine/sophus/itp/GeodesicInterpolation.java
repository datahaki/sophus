// code by gjoel, jph
package ch.alpine.sophus.itp;

import java.io.Serializable;
import java.util.Objects;

import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Scalars;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.itp.AbstractInterpolation;
import ch.alpine.tensor.itp.BinaryAverage;
import ch.alpine.tensor.itp.Interpolation;
import ch.alpine.tensor.sca.Floor;

/** univariate geodesic interpolation
 * 
 * for bivariate or higher interpolation use {@link BiinvariantMeanInterpolation} */
public class GeodesicInterpolation extends AbstractInterpolation implements Serializable {
  /** @param binaryAverage
   * @param tensor
   * @return interpolation function for scalars from the interval [0, tensor.length() - 1] */
  public static Interpolation of(BinaryAverage binaryAverage, Tensor tensor) {
    return new GeodesicInterpolation( //
        Objects.requireNonNull(binaryAverage), //
        Objects.requireNonNull(tensor));
  }

  /***************************************************/
  private final BinaryAverage binaryAverage;
  private final Tensor tensor;

  private GeodesicInterpolation(BinaryAverage binaryAverage, Tensor tensor) {
    this.binaryAverage = binaryAverage;
    this.tensor = tensor;
  }

  @Override // from Interpolation
  public Tensor get(Tensor index) {
    throw new UnsupportedOperationException();
  }

  @Override // from Interpolation
  public Tensor at(Scalar index) {
    Scalar floor = Floor.FUNCTION.apply(index);
    Scalar remain = index.subtract(floor);
    int below = floor.number().intValue();
    if (Scalars.isZero(remain))
      return tensor.get(below);
    return binaryAverage.split( //
        tensor.get(below), //
        tensor.get(below + 1), //
        remain);
  }
}

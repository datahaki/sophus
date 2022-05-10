// code by jph
package ch.alpine.sophus.decim;

import java.util.Objects;

import ch.alpine.sophus.hs.HomogeneousSpace;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.TensorUnaryOperator;

/** http://vixra.org/abs/1909.0174 */
public interface CurveDecimation extends TensorUnaryOperator {
  /** @param hsManifold
   * @param epsilon non-negative
   * @return */
  public static CurveDecimation of(HomogeneousSpace hsManifold, Scalar epsilon) {
    return new RamerDouglasPeucker(new HsLineDistance(hsManifold), epsilon);
  }

  /** @param hsManifold
   * @param epsilon non-negative
   * @return */
  public static CurveDecimation symmetric(HomogeneousSpace hsManifold, Scalar epsilon) {
    return new RamerDouglasPeucker( //
        new SymmetricLineDistance(new HsLineDistance(hsManifold)), //
        epsilon);
  }

  /** @param lineDistance
   * @param epsilon non-negative
   * @return */
  public static CurveDecimation of(LineDistance lineDistance, Scalar epsilon) {
    return new RamerDouglasPeucker(Objects.requireNonNull(lineDistance), epsilon);
  }

  // ---
  /** @param tensor
   * @return */
  DecimationResult evaluate(Tensor tensor);
}

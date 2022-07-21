// code by jph
package ch.alpine.sophus.decim;

import java.util.Objects;

import ch.alpine.sophus.hs.HomogeneousSpace;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.TensorUnaryOperator;

/** <a href="http://vixra.org/abs/1909.0174">1909.0174</a> */
public interface CurveDecimation extends TensorUnaryOperator {
  /** @param homogeneousSpace
   * @param epsilon non-negative
   * @return */
  static CurveDecimation of(HomogeneousSpace homogeneousSpace, Scalar epsilon) {
    return new RamerDouglasPeucker(new HsLineDistance(homogeneousSpace), epsilon);
  }

  /** @param homogeneousSpace
   * @param epsilon non-negative
   * @return */
  static CurveDecimation symmetric(HomogeneousSpace homogeneousSpace, Scalar epsilon) {
    return new RamerDouglasPeucker( //
        new SymmetricLineDistance(new HsLineDistance(homogeneousSpace)), //
        epsilon);
  }

  /** @param lineDistance
   * @param epsilon non-negative
   * @return */
  static CurveDecimation of(LineDistance lineDistance, Scalar epsilon) {
    return new RamerDouglasPeucker(Objects.requireNonNull(lineDistance), epsilon);
  }

  // ---
  /** @param tensor
   * @return */
  DecimationResult evaluate(Tensor tensor);
}

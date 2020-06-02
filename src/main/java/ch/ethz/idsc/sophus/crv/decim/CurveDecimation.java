// code by jph
package ch.ethz.idsc.sophus.crv.decim;

import java.util.Objects;

import ch.ethz.idsc.sophus.hs.VectorLogManifold;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;

/** http://vixra.org/abs/1909.0174 */
public interface CurveDecimation extends TensorUnaryOperator {
  /** @param vectorLogManifold
   * @param epsilon non-negative
   * @return */
  public static CurveDecimation of(VectorLogManifold vectorLogManifold, Scalar epsilon) {
    return new RamerDouglasPeucker(new HsLineDistance(vectorLogManifold), epsilon);
  }

  /** @param vectorLogManifold
   * @param epsilon non-negative
   * @return */
  public static CurveDecimation symmetric(VectorLogManifold vectorLogManifold, Scalar epsilon) {
    return new RamerDouglasPeucker( //
        new SymmetricLineDistance(new HsLineDistance(vectorLogManifold)), //
        epsilon);
  }

  /** @param lineDistance
   * @param epsilon non-negative
   * @return */
  public static CurveDecimation of(LineDistance lineDistance, Scalar epsilon) {
    return new RamerDouglasPeucker(Objects.requireNonNull(lineDistance), epsilon);
  }

  /***************************************************/
  public static interface Result {
    /** @return points in the decimated sequence */
    Tensor result();

    /** @return vector with length of the original sequence */
    Tensor errors();
  }

  /***************************************************/
  /** @param tensor
   * @return */
  Result evaluate(Tensor tensor);
}

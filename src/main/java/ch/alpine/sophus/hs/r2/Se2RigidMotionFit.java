// code by jph
package ch.alpine.sophus.hs.r2;

import ch.alpine.sophus.fit.RigidMotionFit;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.TensorRuntimeException;
import ch.alpine.tensor.Unprotect;
import ch.alpine.tensor.alg.VectorQ;
import ch.alpine.tensor.sca.tri.ArcTan;

public enum Se2RigidMotionFit {
  ;
  /** @param points with dimensions N x 2
   * @param target with dimensions N x 2
   * @return */
  public static Tensor of(Tensor points, Tensor target) {
    if (Unprotect.dimension1Hint(points) == 2)
      return of(RigidMotionFit.of(points, target));
    throw TensorRuntimeException.of(points);
  }

  /** @param points with dimensions N x 2
   * @param target with dimensions N x 2
   * @param weights with dimensions N with entries that sum up to 1
   * @return */
  public static Tensor of(Tensor points, Tensor target, Tensor weights) {
    return of(RigidMotionFit.of(points, target, weights));
  }

  /** @param rigidMotionFit
   * @return vector of length 3 */
  private static Tensor of(RigidMotionFit rigidMotionFit) {
    Tensor rotation = rigidMotionFit.rotation(); // 2 x 2
    Scalar angle = ArcTan.of(rotation.Get(0, 0), rotation.Get(1, 0));
    return VectorQ.requireLength(rigidMotionFit.translation(), 2).append(angle);
  }
}

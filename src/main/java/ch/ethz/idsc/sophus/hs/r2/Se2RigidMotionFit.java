// code by jph
package ch.ethz.idsc.sophus.hs.r2;

import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.Append;
import ch.ethz.idsc.tensor.alg.VectorQ;
import ch.ethz.idsc.tensor.opt.rn.RigidMotionFit;
import ch.ethz.idsc.tensor.sca.ArcTan;

public enum Se2RigidMotionFit {
  ;
  /** @param points
   * @param target
   * @return */
  public static Tensor of(Tensor points, Tensor target) {
    return of(RigidMotionFit.of(points, target));
  }

  /** @param points
   * @param target
   * @param weights
   * @return */
  public static Tensor of(Tensor points, Tensor target, Tensor weights) {
    return of(RigidMotionFit.of(points, target, weights));
  }

  /** @param rigidMotionFit
   * @return vector of length 3 */
  private static Tensor of(RigidMotionFit rigidMotionFit) {
    Tensor rotation = rigidMotionFit.rotation(); // 2 x 2
    return Append.of( //
        VectorQ.requireLength(rigidMotionFit.translation(), 2), //
        ArcTan.of(rotation.Get(0, 0), rotation.Get(1, 0)));
  }
}

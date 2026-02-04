// code by jph
package ch.alpine.sophus.lie.se2;

import java.util.Optional;

import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.lie.rot.Cross;
import ch.alpine.tensor.sca.Chop;

public enum Se2ExpFixpoint {
  ;
  /** @param velocity {vx[m*s^-1], vy[m*s^-1], omega[s^-1]}
   * @return point {px[m], py[m]} in the plane that is fixed by the group action exp(t velocity) for any t */
  public static Optional<Tensor> of(Tensor velocity) {
    return of(velocity, Chop.NONE);
  }

  public static Optional<Tensor> of(Tensor velocity, Chop chop) {
    Scalar omega = velocity.Get(2);
    return chop.isZero(omega) //
        ? Optional.empty()
        : Optional.of(Cross.of(velocity.extract(0, 2)).divide(omega));
  }
}

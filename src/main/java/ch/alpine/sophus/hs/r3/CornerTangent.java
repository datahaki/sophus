// code by jph
package ch.alpine.sophus.hs.r3;

import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.lie.Quaternion;
import ch.alpine.tensor.sca.Chop;

public enum CornerTangent {
  ;
  /** @param qi in Im H
   * @param qj in Im H
   * @param qk in Im H
   * @return */
  public static Quaternion of(Quaternion qi, Quaternion qj, Quaternion qk) {
    Chop.NONE.requireZero(qi.w());
    Chop.NONE.requireZero(qj.w());
    Chop.NONE.requireZero(qk.w());
    Scalar qij = qj.subtract(qi);
    Scalar qjk = qk.subtract(qj);
    return (Quaternion) qij.reciprocal().add(qjk.reciprocal());
  }
}

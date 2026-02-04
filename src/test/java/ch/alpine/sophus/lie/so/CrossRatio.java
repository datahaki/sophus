// code by jph
package ch.alpine.sophus.lie.so;

import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.lie.rot.Quaternion;

/** Amir Vaxman, Christian Müller, and Ofir Weber */
public enum CrossRatio {
  ;
  public static Quaternion of(Quaternion qi, Quaternion qj, Quaternion qk, Quaternion ql) {
    Scalar qij = qj.subtract(qi);
    Scalar qjk = qk.subtract(qj);
    Scalar qkl = ql.subtract(qk);
    Scalar qli = qi.subtract(ql);
    return (Quaternion) qij.divide(qjk).multiply(qkl).divide(qli);
  }
}

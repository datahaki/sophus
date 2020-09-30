// code by jph
package ch.ethz.idsc.sophus.hs.r3;

import ch.ethz.idsc.tensor.Quaternion;
import ch.ethz.idsc.tensor.Scalar;

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

// code by jph
package ch.ethz.idsc.sophus.lie.so3;

import ch.ethz.idsc.sophus.hs.TangentSpace;
import ch.ethz.idsc.sophus.math.Exponential;
import ch.ethz.idsc.tensor.Tensor;

/** elements from the group SO(3) are 3x3 matrices
 * elements from the tangent space TeSO(3) are skew 3x3 matrices */
public enum So3Exponential implements Exponential, TangentSpace {
  INSTANCE;

  /** @throws Exception if vp is not tangent to p */
  @Override // from Exponential
  public Tensor exp(Tensor v) {
    return Rodrigues.INSTANCE.vectorExp(v);
  }

  @Override // from Exponential
  public Tensor log(Tensor p) {
    return Rodrigues.INSTANCE.vectorLog(p);
  }

  @Override // from TangentSpace
  public Tensor vectorLog(Tensor q) {
    return Rodrigues.INSTANCE.vectorLog(q);
  }
}

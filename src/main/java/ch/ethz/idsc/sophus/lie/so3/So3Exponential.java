// code by jph
package ch.ethz.idsc.sophus.lie.so3;

import ch.ethz.idsc.sophus.math.Exponential;
import ch.ethz.idsc.tensor.Tensor;

/** elements from the group SO(3) are 3x3 matrices
 * elements from the tangent space TeSO(3) are vectors of length 3 */
public enum So3Exponential implements Exponential {
  INSTANCE;

  /** @throws Exception if vp is not tangent to p */
  @Override // from Exponential
  public Tensor exp(Tensor vector) {
    return Rodrigues.vectorExp(vector);
  }

  @Override // from Exponential
  public Tensor log(Tensor p) {
    return Rodrigues.INSTANCE.vectorLog(p);
  }
}

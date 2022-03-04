// code by jph
package ch.alpine.sophus.lie.ad;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Transpose;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.sca.Chop;

public enum BianchiIdentity {
  ;
  /** @param rie tensor of rank 4
   * @return array of all-zeros if rie is a Riemannian-curvature tensor */
  public static Tensor of(Tensor rie) {
    return rie // == Transpose.of(rie, 0, 1, 2, 3) // identity
        .add(Transpose.of(rie, 0, 2, 3, 1)) //
        .add(Transpose.of(rie, 0, 3, 1, 2));
  }

  /** @param rie
   * @param chop
   * @return
   * @throws Exception if given tensor does not satisfy the Bianchi identity */
  public static Tensor require(Tensor rie, Chop chop) {
    chop.requireAllZero(of(rie));
    return rie;
  }

  /** @param rie
   * @return given tensor rie
   * @throws Exception if given tensor does not satisfy the Bianchi identity */
  public static Tensor require(Tensor rie) {
    return require(rie, Tolerance.CHOP);
  }
}

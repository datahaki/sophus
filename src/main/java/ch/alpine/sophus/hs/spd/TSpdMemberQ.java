// code by jph
package ch.alpine.sophus.hs.spd;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.chq.MemberQ;
import ch.alpine.tensor.lie.Symmetrize;
import ch.alpine.tensor.mat.SymmetricMatrixQ;

/** predicate does not depend on base point
 * confirmed by chatgpt 20260203 */
public enum TSpdMemberQ implements MemberQ {
  INSTANCE;

  @Override // from MemberQ
  public boolean isMember(Tensor v) {
    return SymmetricMatrixQ.INSTANCE.isMember(v);
  }

  /** @param v
   * @return the symmetric part of v */
  public static Tensor project(Tensor v) {
    return Symmetrize.of(v);
  }
}

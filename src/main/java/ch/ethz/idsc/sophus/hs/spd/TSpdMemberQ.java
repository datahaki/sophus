// code by jph
package ch.ethz.idsc.sophus.hs.spd;

import ch.ethz.idsc.sophus.math.MemberQ;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.lie.Symmetrize;
import ch.ethz.idsc.tensor.mat.SymmetricMatrixQ;

/** predicate does not depend on base point */
public enum TSpdMemberQ implements MemberQ {
  INSTANCE;

  @Override // from MemberQ
  public boolean test(Tensor v) {
    return SymmetricMatrixQ.of(v);
  }

  /** @param v
   * @return the symmetric part of v */
  public static Tensor project(Tensor v) {
    return Symmetrize.of(v);
  }
}

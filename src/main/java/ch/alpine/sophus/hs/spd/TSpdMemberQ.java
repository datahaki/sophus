// code by jph
package ch.alpine.sophus.hs.spd;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.chq.ZeroDefectArrayQ;
import ch.alpine.tensor.chq.ZeroDefectSquareMatrixQ;
import ch.alpine.tensor.lie.Symmetrize;
import ch.alpine.tensor.mat.SymmetricMatrixQ;
import ch.alpine.tensor.sca.Chop;

/** predicate does not depend on base point
 * confirmed by chatgpt 20260203 */
public class TSpdMemberQ extends ZeroDefectSquareMatrixQ {
  public static final ZeroDefectArrayQ INSTANCE = new TSpdMemberQ(Chop._10);

  public TSpdMemberQ(Chop chop) {
    super(chop);
  }

  @Override // from ZeroDefectArrayQ
  public Tensor defect(Tensor tensor) {
    return SymmetricMatrixQ.INSTANCE.defect(tensor);
  }

  /** @param v
   * @return the symmetric part of v */
  public static Tensor project(Tensor v) {
    return Symmetrize.of(v);
  }
}

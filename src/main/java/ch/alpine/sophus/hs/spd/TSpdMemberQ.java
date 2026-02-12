// code by jph
package ch.alpine.sophus.hs.spd;

import java.util.List;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Dimensions;
import ch.alpine.tensor.chq.ZeroDefectArrayQ;
import ch.alpine.tensor.lie.Symmetrize;
import ch.alpine.tensor.mat.SymmetricMatrixQ;
import ch.alpine.tensor.sca.Chop;

/** predicate does not depend on base point
 * confirmed by chatgpt 20260203 */
public class TSpdMemberQ extends ZeroDefectArrayQ {
  private final List<Integer> dims;

  public TSpdMemberQ(Tensor p) {
    super(2, Chop._10);
    dims = Dimensions.of(p);
  }

  @Override // from ZeroDefectArrayQ
  protected boolean isArrayWith(List<Integer> list) {
    return list.equals(dims);
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

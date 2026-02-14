// code by jph
package ch.alpine.sophus.lie.sp;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Transpose;
import ch.alpine.tensor.chq.ZeroDefectArrayQ;
import ch.alpine.tensor.chq.ZeroDefectSquareMatrixQ;
import ch.alpine.tensor.sca.Chop;

class TSpMemberQ extends ZeroDefectSquareMatrixQ {
  public static final ZeroDefectArrayQ INSTANCE = new TSpMemberQ(Chop._10);

  public TSpMemberQ(Chop chop) {
    super(chop);
  }

  @Override // from ZeroDefectArrayQ
  public Tensor defect(Tensor v) {
    int n = v.length() / 2;
    Tensor omega = new SymplecticForm(n).matrix();
    return omega.dot(v).add(Transpose.of(v).dot(omega));
  }
}

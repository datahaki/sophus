// code by jph
package ch.alpine.sophus.lie.sopq;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.chq.ZeroDefectSquareMatrixQ;
import ch.alpine.tensor.mat.TransposeMatrixDot;
import ch.alpine.tensor.sca.Chop;

class TSopqMemberQ extends ZeroDefectSquareMatrixQ {
  private final Tensor form;

  public TSopqMemberQ(int p, int q) {
    super(Chop._08);
    form = ScalarProductForm.of(p, q);
  }

  @Override
  public Tensor defect(Tensor x) {
    // FIXME SOPHUS ALG implement based on /reference
    return TransposeMatrixDot.of(x, form).subtract(form.dot(x));
  }
}

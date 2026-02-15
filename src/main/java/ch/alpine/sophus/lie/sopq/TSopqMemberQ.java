// code by jph
package ch.alpine.sophus.lie.sopq;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Transpose;
import ch.alpine.tensor.chq.ZeroDefectSquareMatrixQ;
import ch.alpine.tensor.sca.Chop;

/* package */ class TSopqMemberQ extends ZeroDefectSquareMatrixQ {
  private final Tensor form;

  public TSopqMemberQ(int p, int q) {
    super(Chop._08);
    form = ScalarProductForm.of(p, q);
  }

  @Override
  public Tensor defect(Tensor x) {
    // FIXME SOPHUS ALG implement based on /reference
    return Transpose.of(x).dot(form).subtract(form.dot(x));
  }
}

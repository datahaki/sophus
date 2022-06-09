// code by jph
package ch.alpine.sophus.lie.sopq;

import java.io.Serializable;

import ch.alpine.sophus.api.MemberQ;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Transpose;
import ch.alpine.tensor.sca.Chop;

public class TSopqMemberQ implements MemberQ, Serializable {
  private static final Chop CHOP = Chop._08;
  // ---
  private final Tensor form;

  public TSopqMemberQ(int p, int q) {
    form = ScalarProductForm.of(p, q);
  }

  @Override // from MemberQ
  public boolean test(Tensor x) {
    // FIXME SOPHUS ALG implement based on /reference
    return CHOP.isClose(Transpose.of(x).dot(form), form.dot(x));
  }
}

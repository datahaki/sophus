// code by jph
package ch.ethz.idsc.sophus.lie.sopq;

import java.io.Serializable;

import ch.ethz.idsc.sophus.math.MemberQ;
import ch.ethz.idsc.sophus.math.ScalarProductForm;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.Transpose;
import ch.ethz.idsc.tensor.mat.Tolerance;

public class SopqMemberQ implements MemberQ, Serializable {
  private final Tensor form;

  public SopqMemberQ(int p, int q) {
    form = ScalarProductForm.of(p, q);
  }

  @Override // from MemberQ
  public boolean test(Tensor x) {
    return Tolerance.CHOP.isClose(Transpose.of(x).dot(form).dot(x), form);
  }
}

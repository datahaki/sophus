// code by jph
package ch.alpine.sophus.hs.gr;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.math.api.FrobeniusForm;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.pdf.RandomSample;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;

class GrBilinearFormTest {
  @Test
  void test() {
    int n = 5;
    Grassmannian grassmannian = new Grassmannian(n, 3);
    Tensor p = RandomSample.of(grassmannian);
    TGrMemberQ tGrMemberQ = new TGrMemberQ(p);
    Tensor u = tGrMemberQ.projection(RandomVariate.of(NormalDistribution.standard(), n, n));
    Tensor v = tGrMemberQ.projection(RandomVariate.of(NormalDistribution.standard(), n, n));
    Scalar f1 = GrBilinearForm.INSTANCE.formEval(u, v);
    Scalar f2 = FrobeniusForm.INSTANCE.formEval(u, v);
    Tolerance.CHOP.requireClose(f1, f2);
  }
}

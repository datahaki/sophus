// code by jph
package ch.alpine.sophus.hs.hn;

import ch.alpine.sophus.lie.sopq.TSopqProject;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.UnitVector;
import ch.alpine.tensor.mat.ex.MatrixExp;
import ch.alpine.tensor.pdf.NormalDistribution;
import ch.alpine.tensor.pdf.RandomVariate;
import junit.framework.TestCase;

public class HnMemberQTest extends TestCase {
  public void testSimple() {
    for (int n = 2; n < 10; ++n)
      HnMemberQ.INSTANCE.require(UnitVector.of(n, n - 1));
    assertFalse(HnMemberQ.INSTANCE.test(UnitVector.of(4, 1)));
  }

  public void testMove() {
    for (int n = 2; n < 10; ++n) {
      Tensor x = RandomVariate.of(NormalDistribution.standard(), n, n);
      x = new TSopqProject(n - 1, 1).apply(x);
      Tensor sopq = MatrixExp.of(x);
      Tensor o = UnitVector.of(n, n - 1);
      HnMemberQ.INSTANCE.require(sopq.dot(o));
    }
  }
}

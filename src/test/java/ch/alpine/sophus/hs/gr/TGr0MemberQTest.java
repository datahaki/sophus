// code by jph
package ch.alpine.sophus.hs.gr;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.mat.DiagonalMatrix;
import ch.alpine.tensor.num.Boole;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.UniformDistribution;
import junit.framework.TestCase;

public class TGr0MemberQTest extends TestCase {
  public void testSimple21() {
    TGr0MemberQ tGr0MemberQ = new TGr0MemberQ(2, 1);
    assertTrue(tGr0MemberQ.test(Tensors.fromString("{{0, 1}, {1, 0}}")));
    assertFalse(tGr0MemberQ.test(Tensors.fromString("{{0, 1}, {2, 0}}")));
    assertFalse(tGr0MemberQ.test(Tensors.fromString("{{0, 1}, {1, 1}}")));
    assertFalse(tGr0MemberQ.test(Tensors.fromString("{{0, 1}, {-1, 0}}")));
  }

  public void testSimple31() {
    TGr0MemberQ tGr0MemberQ = new TGr0MemberQ(3, 1);
    assertTrue(tGr0MemberQ.test(Tensors.fromString("{{0, 1, 0}, {1, 0, 0}, {0, 0, 0}}")));
    assertFalse(tGr0MemberQ.test(Tensors.fromString("{{0, 0, 0}, {0, 0, 1}, {0, 1, 0}}")));
  }

  public void testSimple32() {
    TGr0MemberQ tGr0MemberQ = new TGr0MemberQ(3, 2);
    assertFalse(tGr0MemberQ.test(Tensors.fromString("{{0, 1}, {1, 0}}")));
    assertFalse(tGr0MemberQ.test(Tensors.fromString("{{0, 1, 0}, {1, 0, 0}, {0, 0, 0}}")));
    assertTrue(tGr0MemberQ.test(Tensors.fromString("{{0, 0, 0}, {0, 0, 1}, {0, 1, 0}}")));
  }

  public void testRandom32() {
    int n = 3;
    Distribution distribution = UniformDistribution.unit();
    for (int k = 0; k <= n; ++k) {
      TGr0MemberQ tGr0MemberQ = new TGr0MemberQ(n, k);
      Tensor matrix = tGr0MemberQ.project(RandomVariate.of(distribution, n, n));
      tGr0MemberQ.require(matrix);
      int fk = k;
      Tensor o = DiagonalMatrix.with(Tensors.vector(i -> Boole.of(i < fk), n));
      new TGrMemberQ(o).require(matrix);
    }
  }

  public void testRandom52() {
    int n = 5;
    Distribution distribution = UniformDistribution.unit();
    for (int k = 0; k <= n; ++k) {
      TGr0MemberQ tGr0MemberQ = new TGr0MemberQ(n, k);
      Tensor matrix = tGr0MemberQ.project(RandomVariate.of(distribution, n, n));
      tGr0MemberQ.require(matrix);
      int fk = k;
      Tensor o = DiagonalMatrix.with(Tensors.vector(i -> Boole.of(i < fk), n));
      new TGrMemberQ(o).require(matrix);
    }
  }
}

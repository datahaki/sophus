package ch.alpine.sophus.lie.sp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import ch.alpine.sophus.hs.Exponential;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.mat.IdentityMatrix;
import ch.alpine.tensor.mat.pi.LinearSubspace;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;

class TSpMemberQTest {
  @ParameterizedTest
  @ValueSource(ints = { 1, 2, 3, 5 })
  void test(int n) {
    TSpMemberQ tSpMemberQ = new TSpMemberQ(n);
    LinearSubspace linearSubspace = LinearSubspace.of(tSpMemberQ::defect, 2 * n, 2 * n);
    int dim = linearSubspace.dimensions();
    assertEquals(dim, n * (2 * n + 1));
    Tensor w = RandomVariate.of(NormalDistribution.standard(), dim);
    Tensor v = w.dot(linearSubspace.basis());
    Symplectic symplectic = new Symplectic(n);
    Exponential tangentSpace = symplectic.exponential(IdentityMatrix.of(2 * n));
    Tensor p = tangentSpace.exp(v);
    assertTrue(symplectic.isMember(p));
  }
}

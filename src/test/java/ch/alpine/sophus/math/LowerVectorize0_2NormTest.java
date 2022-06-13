// code by jph
package ch.alpine.sophus.math;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.lie.Symmetrize;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.nrm.FrobeniusNorm;
import ch.alpine.tensor.num.Pi;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.UniformDistribution;

class LowerVectorize0_2NormTest {
  @Test
  void testSimple() {
    for (int n = 1; n < 10; ++n) {
      Tensor matrix = Symmetrize.of(RandomVariate.of(UniformDistribution.unit(), n, n));
      Scalar n1 = FrobeniusNorm.of(matrix);
      Scalar n2 = LowerVectorize0_2Norm.INSTANCE.norm(LowerVectorize.of(matrix, 0));
      Tolerance.CHOP.requireClose(n1, n2);
    }
  }

  @Test
  void testLengthFail() {
    assertThrows(Exception.class, () -> LowerVectorize0_2Norm.INSTANCE.norm(Pi.VALUE));
    assertThrows(Exception.class, () -> LowerVectorize0_2Norm.INSTANCE.norm(Tensors.vector()));
    assertThrows(Exception.class, () -> LowerVectorize0_2Norm.INSTANCE.norm(Tensors.vector(1, 2)));
    assertThrows(Exception.class, () -> LowerVectorize0_2Norm.INSTANCE.norm(Tensors.vector(1, 2, 3, 4)));
  }

  @Test
  void testRequireTriangleNumber() {
    LowerVectorize0_2Norm.requireTriangleNumber(1);
    LowerVectorize0_2Norm.requireTriangleNumber(3);
    LowerVectorize0_2Norm.requireTriangleNumber(6);
    LowerVectorize0_2Norm.requireTriangleNumber(10);
    assertThrows(Exception.class, () -> LowerVectorize0_2Norm.requireTriangleNumber(2));
    assertThrows(Exception.class, () -> LowerVectorize0_2Norm.requireTriangleNumber(4));
  }
}

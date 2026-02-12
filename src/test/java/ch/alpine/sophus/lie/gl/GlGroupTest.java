// code by jph
package ch.alpine.sophus.lie.gl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import ch.alpine.sophus.lie.se2.Se2Group;
import ch.alpine.sophus.lie.se2.Se2Matrix;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Array;
import ch.alpine.tensor.lie.rot.RotationMatrix;
import ch.alpine.tensor.mat.DiagonalMatrix;
import ch.alpine.tensor.mat.HilbertMatrix;
import ch.alpine.tensor.mat.IdentityMatrix;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.sca.Chop;

class GlGroupTest {
  @Test
  void testSimple() {
    Tensor result = GlGroup.INSTANCE.combine( //
        RotationMatrix.of(RealScalar.of(0.24)), RotationMatrix.of(RealScalar.of(-0.24)));
    Tolerance.CHOP.requireClose(result, IdentityMatrix.of(2));
    assertTrue(GlGroup.INSTANCE.toString().startsWith("GL"));
  }

  @ParameterizedTest
  @ValueSource(ints = { 3, 5 })
  void testSimple(int n) {
    Tensor matrix = RandomVariate.of(NormalDistribution.standard(), n, n);
    Tensor result = GlGroup.INSTANCE.diffOp(matrix).apply(matrix);
    Chop._10.requireClose(result, IdentityMatrix.of(n));
    Chop._10.requireClose(result, GlGroup.INSTANCE.neutral(matrix));
  }

  @Test
  void testSerializable() {
    Tensor tensor = DiagonalMatrix.of(1, 2, 3);
    Tensor result = GlGroup.INSTANCE.diffOp(tensor).apply(tensor);
    assertEquals(result, IdentityMatrix.of(3));
  }

  @Test
  void testLinearGroupSe2() {
    Distribution distribution = NormalDistribution.standard();
    for (int count = 0; count < 10; ++count) {
      Tensor g = RandomVariate.of(distribution, 3);
      Tensor uvw = RandomVariate.of(distribution, 3);
      Tensor adjoint = Se2Group.INSTANCE.adjoint(g, uvw);
      Tensor linear = Se2Matrix.of(g);
      Tensor X = Tensors.matrix(new Scalar[][] { //
          { RealScalar.ZERO, uvw.Get(2).negate(), uvw.Get(0) }, //
          { uvw.Get(2), RealScalar.ZERO, uvw.Get(1) }, //
          { RealScalar.ZERO, RealScalar.ZERO, RealScalar.ZERO } });
      Tensor tensor = GlGroup.INSTANCE.adjoint(linear, X);
      Tensor xya = Tensors.of(tensor.Get(0, 2), tensor.Get(1, 2), tensor.Get(1, 0));
      Chop._12.requireClose(adjoint, xya);
    }
  }

  @Test
  void testAdjoint() {
    Tensor xya = Tensors.vector(1, 2, 3);
    Tensor matrix = Se2Matrix.of(xya);
    Tensor adjointGl = GlGroup.INSTANCE.adjoint(matrix, Tensors.fromString("{{0, 1, 0}, {-1, 0, 0}, {0, 0, 0}}")).maps(Chop._10);
    Tensor adjointSe = Se2Group.INSTANCE.adjoint(xya, Tensors.vector(0, 0, -1));
    Chop._12.requireClose(adjointGl.get(0, 2), adjointSe.get(0));
    Chop._12.requireClose(adjointGl.get(1, 2), adjointSe.get(1));
    Chop._12.requireClose(adjointGl.get(1, 0), adjointSe.get(2));
  }

  @Test
  void testIsMember() {
    assertTrue(GlGroup.INSTANCE.isPointQ().isMember(IdentityMatrix.of(2)));
    assertFalse(GlGroup.INSTANCE.isPointQ().isMember(Array.zeros(2, 2)));
    assertFalse(GlGroup.INSTANCE.isPointQ().isMember(Array.zeros(2, 3)));
  }

  @Test
  void testAdjointFail() {
    assertThrows(Exception.class, () -> GlGroup.INSTANCE.adjoint(IdentityMatrix.of(5), Tensors.vector(1, 2, 3, 4, 5)));
  }

  @Test
  void testCombineNonSquareFail() {
    Tensor p = DiagonalMatrix.of(1, 2);
    assertThrows(Exception.class, () -> GlGroup.INSTANCE.combine(p, HilbertMatrix.of(2, 3)));
    assertThrows(Exception.class, () -> GlGroup.INSTANCE.combine(p, Tensors.vector(1, 2)));
  }
}

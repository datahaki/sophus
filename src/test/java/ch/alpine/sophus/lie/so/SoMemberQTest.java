// code by jph
package ch.alpine.sophus.lie.so;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.lie.TensorWedge;
import ch.alpine.tensor.mat.DiagonalMatrix;
import ch.alpine.tensor.mat.HilbertMatrix;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.mat.pd.Orthogonalize;
import ch.alpine.tensor.mat.re.Det;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;

class SoMemberQTest {
  @Test
  void testSimple() {
    Tensor wedge = TensorWedge.of(Tensors.vector(1, 2, 3), Tensors.vector(-1, 4, 0.2));
    TSoMemberQ.INSTANCE.require(wedge);
    assertFalse(TSoMemberQ.INSTANCE.test(HilbertMatrix.of(3)));
  }

  @Test
  void testDet1() {
    Tensor nondet = DiagonalMatrix.of(1, 1, -1);
    assertEquals(Det.of(nondet), RealScalar.ONE.negate());
    assertFalse(SoMemberQ.INSTANCE.test(nondet));
  }

  @ParameterizedTest
  @ValueSource(ints = { 1, 2, 3, 4, 5 })
  void testProject(int n) {
    Tensor x = RandomVariate.of(NormalDistribution.standard(), n, n);
    Tensor matrix = SoMemberQ.project(x);
    Scalar scalar = Det.of(matrix);
    Tolerance.CHOP.requireClose(scalar, RealScalar.ONE);
    Tensor polard = Orthogonalize.usingPD(matrix); // does not work with x
    Tolerance.CHOP.requireClose(matrix, polard);
  }

  @Test
  void testNullFail() {
    assertThrows(Exception.class, () -> TSoMemberQ.INSTANCE.test(null));
  }
}

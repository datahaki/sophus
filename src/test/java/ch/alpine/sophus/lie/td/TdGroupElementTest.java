// code by ob
package ch.alpine.sophus.lie.td;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Append;
import ch.alpine.tensor.chq.ExactTensorQ;
import ch.alpine.tensor.sca.Chop;

class TdGroupElementTest {
  @Test
  void testSt1Inverse() {
    Tensor p = Tensors.vector(3, 6);
    Tensor id = Tensors.vector(0, 1);
    TdGroupElement pE = TdGroup.INSTANCE.element(p);
    assertEquals(pE.inverse().combine(p), id);
  }

  @Test
  void testSt1Combine() {
    Tensor p = Tensors.vector(6, 3);
    TdGroupElement pE = TdGroup.INSTANCE.element(p);
    Tensor q = Tensors.vector(8, 2);
    assertEquals(pE.combine(q), Tensors.vector(3 * 8 + 6, 2 * 3));
  }

  @Test
  void testInverse() {
    Tensor id = Tensors.vector(0, 0, 0, 0, 1);
    for (int count = 0; count < 100; ++count) {
      Scalar lambda = RealScalar.of(Math.random() + 0.001);
      Tensor t = Tensors.vector(Math.random(), 32 * Math.random(), -Math.random(), -17 * Math.random());
      Tensor p = Append.of(t, lambda);
      TdGroupElement pE = TdGroup.INSTANCE.element(p);
      Chop._11.requireClose(pE.inverse().combine(p), id);
    }
  }

  @Test
  void testCombine() {
    Scalar lambda = RealScalar.of(2);
    Tensor t = Tensors.vector(0, 1, -2);
    Tensor p = Append.of(t, lambda);
    TdGroupElement pE = TdGroup.INSTANCE.element(p);
    Scalar lambda2 = RealScalar.of(2);
    Tensor t2 = Tensors.vector(2, 3, 4);
    Tensor q = Append.of(t2, lambda2);
    assertEquals(pE.combine(q), Tensors.vector(4, 7, 6, 4));
  }

  @Test
  void testAdjoint() {
    TdGroupElement pE = TdGroup.INSTANCE.element(Tensors.vector(1, 2, 3, 4));
    Tensor adjoint = pE.adjoint(Tensors.vector(5, 7, 8, 2));
    assertEquals(adjoint, Tensors.vector(18, 24, 26, 2));
    ExactTensorQ.require(adjoint);
  }

  // checks that lambda is required to be positive
  @Test
  void testLambdaNonPositiveFail() {
    assertThrows(Exception.class, () -> TdGroup.INSTANCE.element(Tensors.vector(5, 0)));
    assertThrows(Exception.class, () -> TdGroup.INSTANCE.element(Tensors.vector(5, -1)));
  }

  @Test
  void testCombineFail() {
    TdGroupElement pE = TdGroup.INSTANCE.element(Tensors.vector(1, 2, 3, 4));
    assertThrows(Exception.class, () -> pE.combine(Tensors.vector(1, 2, 3, 0)));
    assertThrows(Exception.class, () -> pE.combine(Tensors.vector(1, 2, 3, 4, 1)));
  }

  @Test
  void testDlNullFail() {
    TdGroupElement pE = TdGroup.INSTANCE.element(Tensors.vector(1, 2, 3, 4));
    assertThrows(Exception.class, () -> pE.dL(null));
  }
}

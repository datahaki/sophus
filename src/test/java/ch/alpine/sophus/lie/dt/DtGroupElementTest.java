// code by ob
package ch.alpine.sophus.lie.dt;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.usr.AssertFail;
import ch.alpine.tensor.ExactTensorQ;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.sca.Chop;

public class DtGroupElementTest {
  @Test
  public void testSt1Inverse() {
    Tensor p = Tensors.vector(3, 6);
    Tensor id = Tensors.vector(1, 0);
    DtGroupElement pE = DtGroup.INSTANCE.element(p);
    assertEquals(pE.inverse().combine(p), id);
  }

  @Test
  public void testSt1Combine() {
    Tensor p = Tensors.vector(3, 6);
    DtGroupElement pE = DtGroup.INSTANCE.element(p);
    Tensor q = Tensors.vector(2, 8);
    assertEquals(pE.combine(q), Tensors.vector(2 * 3, 3 * 8 + 6));
  }

  @Test
  public void testInverse() {
    Tensor id = Tensors.of(RealScalar.ONE, Tensors.vector(0, 0, 0, 0));
    for (int count = 0; count < 100; ++count) {
      Scalar lambda = RealScalar.of(Math.random() + 0.001);
      Tensor t = Tensors.vector(Math.random(), 32 * Math.random(), -Math.random(), -17 * Math.random());
      Tensor p = Tensors.of(lambda, t);
      DtGroupElement pE = DtGroup.INSTANCE.element(p);
      Chop._11.requireClose(pE.inverse().combine(p), id);
    }
  }

  @Test
  public void testCombine() {
    Scalar lambda = RealScalar.of(2);
    Tensor t = Tensors.vector(0, 1, -2);
    Tensor p = Tensors.of(lambda, t);
    DtGroupElement pE = DtGroup.INSTANCE.element(p);
    Scalar lambda2 = RealScalar.of(2);
    Tensor t2 = Tensors.vector(2, 3, 4);
    Tensor q = Tensors.of(lambda2, t2);
    assertEquals(pE.combine(q), Tensors.of(RealScalar.of(4), Tensors.vector(4, 7, 6)));
  }

  @Test
  public void testAdjoint() {
    DtGroupElement pE = DtGroup.INSTANCE.element(Tensors.fromString("{4, {1, 2, 3}}"));
    Tensor adjoint = pE.adjoint(Tensors.fromString("{2, {5, 7, 8}}"));
    assertEquals(adjoint, Tensors.fromString("{2, {18, 24, 26}}"));
    ExactTensorQ.require(adjoint);
  }

  // checks that lambda is required to be positive
  @Test
  public void testLambdaNonPositiveFail() {
    AssertFail.of(() -> DtGroup.INSTANCE.element(Tensors.vector(0, 5)));
    AssertFail.of(() -> DtGroup.INSTANCE.element(Tensors.vector(-1, 5)));
  }

  @Test
  public void testCombineFail() {
    DtGroupElement pE = DtGroup.INSTANCE.element(Tensors.fromString("{4, {1, 2, 3}}"));
    AssertFail.of(() -> pE.combine(Tensors.fromString("{0, {1, 2, 3}}")));
    AssertFail.of(() -> pE.combine(Tensors.fromString("{1, {1, 2, 3, 4}}")));
  }

  @Test
  public void testDlNullFail() {
    DtGroupElement pE = DtGroup.INSTANCE.element(Tensors.fromString("{4, {1, 2, 3}}"));
    AssertFail.of(() -> pE.dL(null));
  }
}

// code by jph
package ch.alpine.sophus.lie.so;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.lie.TensorWedge;
import ch.alpine.tensor.mat.DiagonalMatrix;
import ch.alpine.tensor.mat.HilbertMatrix;
import ch.alpine.tensor.mat.re.Det;

class SoMemberQTest {
  @Test
  public void testSimple() {
    Tensor wedge = TensorWedge.of(Tensors.vector(1, 2, 3), Tensors.vector(-1, 4, 0.2));
    TSoMemberQ.INSTANCE.require(wedge);
    assertFalse(TSoMemberQ.INSTANCE.test(HilbertMatrix.of(3)));
  }

  @Test
  public void testDet1() {
    Tensor nondet = DiagonalMatrix.of(1, 1, -1);
    assertEquals(Det.of(nondet), RealScalar.ONE.negate());
    assertFalse(SoMemberQ.INSTANCE.test(nondet));
  }

  @Test
  public void testNullFail() {
    assertThrows(Exception.class, () -> TSoMemberQ.INSTANCE.test(null));
  }
}

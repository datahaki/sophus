// code by jph
package ch.alpine.sophus.hs.r3;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.mat.IdentityMatrix;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.sca.Chop;

class NylanderPowerTest {
  @Test
  public void testZero() {
    for (int exp = 0; exp < 5; ++exp)
      assertEquals(NylanderPower.of(Tensors.vector(0, 0, 0), exp), Tensors.vector(0, 0, exp == 0 ? 1 : 0));
  }

  @Test
  public void testEZero() {
    Distribution distribution = NormalDistribution.standard();
    for (int n = 0; n < 20; ++n) {
      assertEquals( //
          NylanderPower.of(RandomVariate.of(distribution, 3), 0), Tensors.vector(0, 0, 1));
    }
  }

  @Test
  public void testSpecial() {
    Tensor expected = Tensors.vector(-78.82060109268512, 32.91199889151254, -44.74536203838224);
    Tensor actual = NylanderPower.of(Tensors.vector(1.12, -2.23, 3.34), 3.2);
    Tolerance.CHOP.requireClose(expected, actual);
  }

  @Test
  public void testSpecialNegative() {
    Tensor expected = Tensors.vector(0.008477125267545767, 0.003539672795448542, -0.00481234644093896);
    Tensor actual = NylanderPower.of(Tensors.vector(1.12, -2.23, 3.34), -3.2);
    Tolerance.CHOP.requireClose(expected, actual);
  }

  @Test
  public void testSpecial8() {
    Tensor expected = Tensors.vector(69616.37623576757, 45802.24276062281, 37310.875885353504);
    Tensor actual = NylanderPower.of(Tensors.vector(1.12, -2.23, 3.34), 8);
    Chop._08.requireClose(expected, actual);
  }

  @Test
  public void testMatrixFail() {
    assertThrows(Exception.class, () -> NylanderPower.of(IdentityMatrix.of(3), 3));
  }

  @Test
  public void testLengthFail() {
    assertThrows(Exception.class, () -> NylanderPower.of(Tensors.vector(1, 2, 3, 4), 3));
  }

  @Test
  public void testFailNull() {
    assertThrows(Exception.class, () -> NylanderPower.of(Tensors.vector(1, 2, 3), (Scalar) null));
  }
}

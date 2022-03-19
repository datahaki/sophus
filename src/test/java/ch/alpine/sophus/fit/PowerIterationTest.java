// code by jph
package ch.alpine.sophus.fit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.DoubleScalar;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Array;
import ch.alpine.tensor.lie.r2.RotationMatrix;
import ch.alpine.tensor.mat.HilbertMatrix;
import ch.alpine.tensor.mat.ev.Eigensystem;
import ch.alpine.tensor.sca.Abs;
import ch.alpine.tensor.sca.Chop;

public class PowerIterationTest {
  @Test
  public void testSymmetric() {
    Tensor matrix = Tensors.fromString("{{2, 3, 0, 1}, {3, 1, 7, 5}, {0, 7, 10, 9}, {1, 5, 9, 13}}");
    Eigensystem eigensystem = Eigensystem.ofSymmetric(matrix);
    Tensor v = eigensystem.vectors().get(0).unmodifiable();
    Tensor x = PowerIteration.of(matrix).get();
    Chop._12.requireClose(Abs.of(x.dot(v)), RealScalar.ONE);
  }

  @Test
  public void testNegative() {
    Tensor matrix = Tensors.fromString("{{-1, 0}, {0, 0}}");
    Tensor x = PowerIteration.of(matrix).get();
    assertEquals(Abs.of(x.Get(0)), RealScalar.ONE);
    assertEquals(Abs.of(x.Get(1)), RealScalar.ZERO);
  }

  @Test
  public void testScalar() {
    Tensor matrix = Tensors.fromString("{{2}}");
    Eigensystem eigensystem = Eigensystem.ofSymmetric(matrix);
    Tensor v = eigensystem.vectors().get(0).unmodifiable();
    assertEquals(Abs.of(v.Get(0)), RealScalar.ONE);
    Tensor x = PowerIteration.of(matrix).get();
    assertEquals(Abs.of(x.Get(0)), RealScalar.ONE);
  }

  @Test
  public void testRotationFail() {
    assertFalse(PowerIteration.of(RotationMatrix.of(DoubleScalar.of(0.3))).isPresent());
  }

  @Test
  public void testZerosFail() {
    assertThrows(Exception.class, () -> PowerIteration.of(Array.zeros(3, 3)));
  }

  @Test
  public void testVectorFail() {
    assertThrows(Exception.class, () -> PowerIteration.of(Tensors.vector(1, 2, 3)));
  }

  @Test
  public void testMatrixFail() {
    assertThrows(Exception.class, () -> PowerIteration.of(HilbertMatrix.of(4, 3)));
    assertThrows(Exception.class, () -> PowerIteration.of(HilbertMatrix.of(3, 4)));
  }
}

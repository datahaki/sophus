// code by jph
package ch.alpine.sophus.fit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.DoubleScalar;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Array;
import ch.alpine.tensor.lie.r2.RotationMatrix;
import ch.alpine.tensor.mat.HilbertMatrix;
import ch.alpine.tensor.mat.ev.Eigensystem;
import ch.alpine.tensor.sca.Abs;
import ch.alpine.tensor.sca.Chop;

class PowerIterationTest {
  @Test
  void testSymmetric() {
    Tensor matrix = Tensors.fromString("{{2, 3, 0, 1}, {3, 1, 7, 5}, {0, 7, 10, 9}, {1, 5, 9, 13}}");
    Eigensystem eigensystem = Eigensystem.ofSymmetric(matrix);
    Tensor v = eigensystem.vectors().get(0).unmodifiable();
    Tensor x = PowerIteration.of(matrix).get();
    Chop._12.requireClose(Abs.FUNCTION.apply((Scalar) x.dot(v)), RealScalar.ONE);
  }

  @Test
  void testNegative() {
    Tensor matrix = Tensors.fromString("{{-1, 0}, {0, 0}}");
    Tensor x = PowerIteration.of(matrix).get();
    assertEquals(Abs.FUNCTION.apply(x.Get(0)), RealScalar.ONE);
    assertEquals(Abs.FUNCTION.apply(x.Get(1)), RealScalar.ZERO);
  }

  @Test
  void testScalar() {
    Tensor matrix = Tensors.fromString("{{2}}");
    Eigensystem eigensystem = Eigensystem.ofSymmetric(matrix);
    Tensor v = eigensystem.vectors().get(0).unmodifiable();
    assertEquals(Abs.FUNCTION.apply(v.Get(0)), RealScalar.ONE);
    Tensor x = PowerIteration.of(matrix).get();
    assertEquals(Abs.FUNCTION.apply(x.Get(0)), RealScalar.ONE);
  }

  @Test
  void testRotationFail() {
    assertFalse(PowerIteration.of(RotationMatrix.of(DoubleScalar.of(0.3))).isPresent());
  }

  @Test
  void testZerosFail() {
    assertThrows(Exception.class, () -> PowerIteration.of(Array.zeros(3, 3)));
  }

  @Test
  void testVectorFail() {
    assertThrows(Exception.class, () -> PowerIteration.of(Tensors.vector(1, 2, 3)));
  }

  @Test
  void testMatrixFail() {
    assertThrows(Exception.class, () -> PowerIteration.of(HilbertMatrix.of(4, 3)));
    assertThrows(Exception.class, () -> PowerIteration.of(HilbertMatrix.of(3, 4)));
  }
}

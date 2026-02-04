// code by jph
package ch.alpine.sophus.math;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.mat.HilbertMatrix;

class AffineQTest {
  @Test
  void testRequire() {
    AffineQ.INSTANCE.requireMember(Tensors.vector(0.5, 0.5));
    AffineQ.INSTANCE.requireMember(Tensors.vector(0.25, 0.25, 0.25, 0.25));
    AffineQ.INSTANCE.requireMember(Tensors.vector(1, 0));
    AffineQ.INSTANCE.requireMember(Tensors.vector(-0.5, 1.5));
    AffineQ.INSTANCE.requireMember(Tensors.vector(-0.25, 0.75, 0.25, 0.25));
    AffineQ.INSTANCE.requireMember(Tensors.vector(1, -2, 2));
  }

  @Test
  void testFailScalar() {
    assertThrows(Exception.class, () -> AffineQ.INSTANCE.requireMember(RealScalar.ONE)); // , Chop._08));
  }

  @Test
  void testFailMatrix() {
    assertThrows(Exception.class, () -> AffineQ.INSTANCE.requireMember(HilbertMatrix.of(3)));
  }
}

// code by jph
package ch.alpine.sophus.math;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.bm.AffineVectorQ;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.mat.HilbertMatrix;

class AffineVectorQTest {
  @Test
  void testRequire() {
    AffineVectorQ.INSTANCE.require(Tensors.vector(0.5, 0.5));
    AffineVectorQ.INSTANCE.require(Tensors.vector(0.25, 0.25, 0.25, 0.25));
    AffineVectorQ.INSTANCE.require(Tensors.vector(1, 0));
    AffineVectorQ.INSTANCE.require(Tensors.vector(-0.5, 1.5));
    AffineVectorQ.INSTANCE.require(Tensors.vector(-0.25, 0.75, 0.25, 0.25));
    AffineVectorQ.INSTANCE.require(Tensors.vector(1, -2, 2));
  }

  @Test
  void testFailScalar() {
    assertThrows(Exception.class, () -> AffineVectorQ.INSTANCE.require(RealScalar.ONE)); // , Chop._08));
  }

  @Test
  void testFailMatrix() {
    assertThrows(Exception.class, () -> AffineVectorQ.INSTANCE.require(HilbertMatrix.of(3)));
  }
}

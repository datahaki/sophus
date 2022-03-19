// code by jph
package ch.alpine.sophus.math;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.mat.HilbertMatrix;
import ch.alpine.tensor.sca.Chop;

public class AffineQTest {
  @Test
  public void testRequire() {
    AffineQ.require(Tensors.vector(0.5, 0.5), Chop._08);
    AffineQ.require(Tensors.vector(0.25, 0.25, 0.25, 0.25), Chop._08);
    AffineQ.require(Tensors.vector(1, 0), Chop._08);
    AffineQ.require(Tensors.vector(-0.5, 1.5), Chop._08);
    AffineQ.require(Tensors.vector(-0.25, 0.75, 0.25, 0.25), Chop._08);
    AffineQ.require(Tensors.vector(1, -2, 2), Chop._08);
  }

  @Test
  public void testRequirePositive() {
    AffineQ.requirePositiveOrZero(Tensors.vector(0.5, 0.5), Chop._08);
    AffineQ.requirePositiveOrZero(Tensors.vector(0.25, 0.25, 0.25, 0.25), Chop._08);
    AffineQ.requirePositiveOrZero(Tensors.vector(1, 0), Chop._08);
  }

  @Test
  public void testFail() {
    assertThrows(Exception.class, () -> AffineQ.requirePositiveOrZero(Tensors.vector(2, -1), Chop._08));
  }

  @Test
  public void testFail2() {
    assertThrows(Exception.class, () -> AffineQ.requirePositiveOrZero(Tensors.vector(1, 1), Chop._08));
  }

  @Test
  public void testFailScalar() {
    assertThrows(Exception.class, () -> AffineQ.require(RealScalar.ONE, Chop._08));
    assertThrows(Exception.class, () -> AffineQ.requirePositiveOrZero(RealScalar.ONE, Chop._08));
  }

  @Test
  public void testFailMatrix() {
    assertThrows(Exception.class, () -> AffineQ.require(HilbertMatrix.of(3), Chop._08));
    assertThrows(Exception.class, () -> AffineQ.requirePositiveOrZero(HilbertMatrix.of(3), Chop._08));
  }
}

// code by jph
package ch.alpine.sophus.math;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.usr.AssertFail;
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
    AssertFail.of(() -> AffineQ.requirePositiveOrZero(Tensors.vector(2, -1), Chop._08));
  }

  @Test
  public void testFail2() {
    AssertFail.of(() -> AffineQ.requirePositiveOrZero(Tensors.vector(1, 1), Chop._08));
  }

  @Test
  public void testFailScalar() {
    AssertFail.of(() -> AffineQ.require(RealScalar.ONE, Chop._08));
    AssertFail.of(() -> AffineQ.requirePositiveOrZero(RealScalar.ONE, Chop._08));
  }

  @Test
  public void testFailMatrix() {
    AssertFail.of(() -> AffineQ.require(HilbertMatrix.of(3), Chop._08));
    AssertFail.of(() -> AffineQ.requirePositiveOrZero(HilbertMatrix.of(3), Chop._08));
  }
}

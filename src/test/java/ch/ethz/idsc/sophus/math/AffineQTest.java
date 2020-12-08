// code by jph
package ch.ethz.idsc.sophus.math;

import ch.ethz.idsc.sophus.usr.AssertFail;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.mat.HilbertMatrix;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class AffineQTest extends TestCase {
  public void testRequire() {
    AffineQ.require(Tensors.vector(0.5, 0.5), Chop._08);
    AffineQ.require(Tensors.vector(0.25, 0.25, 0.25, 0.25), Chop._08);
    AffineQ.require(Tensors.vector(1, 0), Chop._08);
    AffineQ.require(Tensors.vector(-0.5, 1.5), Chop._08);
    AffineQ.require(Tensors.vector(-0.25, 0.75, 0.25, 0.25), Chop._08);
    AffineQ.require(Tensors.vector(1, -2, 2), Chop._08);
  }

  public void testRequirePositive() {
    AffineQ.requirePositiveOrZero(Tensors.vector(0.5, 0.5), Chop._08);
    AffineQ.requirePositiveOrZero(Tensors.vector(0.25, 0.25, 0.25, 0.25), Chop._08);
    AffineQ.requirePositiveOrZero(Tensors.vector(1, 0), Chop._08);
  }

  public void testFail() {
    AssertFail.of(() -> AffineQ.requirePositiveOrZero(Tensors.vector(2, -1), Chop._08));
  }

  public void testFail2() {
    AssertFail.of(() -> AffineQ.requirePositiveOrZero(Tensors.vector(1, 1), Chop._08));
  }

  public void testFailScalar() {
    AssertFail.of(() -> AffineQ.require(RealScalar.ONE, Chop._08));
    AssertFail.of(() -> AffineQ.requirePositiveOrZero(RealScalar.ONE, Chop._08));
  }

  public void testFailMatrix() {
    AssertFail.of(() -> AffineQ.require(HilbertMatrix.of(3), Chop._08));
    AssertFail.of(() -> AffineQ.requirePositiveOrZero(HilbertMatrix.of(3), Chop._08));
  }
}

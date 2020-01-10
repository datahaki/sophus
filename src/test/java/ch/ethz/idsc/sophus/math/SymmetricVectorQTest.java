// code by jph
package ch.ethz.idsc.sophus.math;

import ch.ethz.idsc.tensor.Tensors;
import junit.framework.TestCase;

public class SymmetricVectorQTest extends TestCase {
  public void testSimple() {
    assertTrue(SymmetricVectorQ.of(Tensors.unmodifiableEmpty()));
    assertTrue(SymmetricVectorQ.of(Tensors.vector(1, 2, 2, 1)));
    assertTrue(SymmetricVectorQ.of(Tensors.vector(1, 2, 1)));
    assertFalse(SymmetricVectorQ.of(Tensors.vector(1, 2, 3)));
    SymmetricVectorQ.require(Tensors.vector(1, 2, 1));
    SymmetricVectorQ.require(Tensors.vector(1, 1, 3, 3, 1, 1));
  }

  public void testNonVector() {
    assertTrue(SymmetricVectorQ.of(Tensors.empty()));
    assertFalse(SymmetricVectorQ.of(Tensors.fromString("{{1}}")));
    assertFalse(SymmetricVectorQ.of(Tensors.fromString("{1, {2}, 1}")));
  }

  public void testThrow() {
    try {
      SymmetricVectorQ.require(Tensors.vector(1, 1, 3, 1, 1, 1));
      fail();
    } catch (Exception exception) {
      // ---
    }
  }
}

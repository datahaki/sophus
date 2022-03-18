// code by jph
package ch.alpine.sophus.math;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.usr.AssertFail;
import ch.alpine.tensor.Tensors;

public class SymmetricVectorQTest {
  @Test
  public void testSimple() {
    assertTrue(SymmetricVectorQ.of(Tensors.empty()));
    assertTrue(SymmetricVectorQ.of(Tensors.vector(1, 2, 2, 1)));
    assertTrue(SymmetricVectorQ.of(Tensors.vector(1, 2, 1)));
    assertFalse(SymmetricVectorQ.of(Tensors.vector(1, 2, 3)));
    SymmetricVectorQ.require(Tensors.vector(1, 2, 1));
    SymmetricVectorQ.require(Tensors.vector(1, 1, 3, 3, 1, 1));
  }

  @Test
  public void testNonVector() {
    assertTrue(SymmetricVectorQ.of(Tensors.empty()));
    assertFalse(SymmetricVectorQ.of(Tensors.fromString("{{1}}")));
    assertFalse(SymmetricVectorQ.of(Tensors.fromString("{1, {2}, 1}")));
  }

  @Test
  public void testThrow() {
    AssertFail.of(() -> SymmetricVectorQ.require(Tensors.vector(1, 1, 3, 1, 1, 1)));
  }
}

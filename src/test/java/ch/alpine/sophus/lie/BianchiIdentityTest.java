// code by jph
package ch.alpine.sophus.lie;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Array;
import ch.alpine.tensor.mat.HilbertMatrix;

public class BianchiIdentityTest {
  @Test
  public void testRequireTrivial() {
    BianchiIdentity.require(Array.zeros(4, 4, 4, 4));
  }

  @Test
  public void testRank1Fail() {
    assertThrows(Exception.class, () -> BianchiIdentity.of(Tensors.vector(1, 2, 3)));
  }

  @Test
  public void testRank2Fail() {
    assertThrows(Exception.class, () -> BianchiIdentity.of(HilbertMatrix.of(3, 3)));
  }

  @Test
  public void testRank3Fail() {
    assertThrows(Exception.class, () -> BianchiIdentity.of(Array.zeros(3, 3, 3)));
  }
}

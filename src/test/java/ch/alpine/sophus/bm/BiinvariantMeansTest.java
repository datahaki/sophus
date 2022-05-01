// code by jph
package ch.alpine.sophus.bm;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.lie.so2.So2CoveringBiinvariantMean;
import ch.alpine.tensor.Tensors;

class BiinvariantMeansTest {
  @Test
  public void testNullFail() {
    assertThrows(Exception.class, () -> BiinvariantMeans.of(null, Tensors.vector(1)));
  }

  @Test
  public void testNonAffineFail() {
    assertThrows(Exception.class, () -> BiinvariantMeans.of(So2CoveringBiinvariantMean.INSTANCE, Tensors.vector(1, 1, 1)));
  }
}

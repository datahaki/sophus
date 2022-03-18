// code by jph
package ch.alpine.sophus.bm;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.lie.so2.So2CoveringBiinvariantMean;
import ch.alpine.sophus.usr.AssertFail;
import ch.alpine.tensor.Tensors;

public class BiinvariantMeansTest {
  @Test
  public void testNullFail() {
    AssertFail.of(() -> BiinvariantMeans.of(null, Tensors.vector(1)));
  }

  @Test
  public void testNonAffineFail() {
    AssertFail.of(() -> BiinvariantMeans.of(So2CoveringBiinvariantMean.INSTANCE, Tensors.vector(1, 1, 1)));
  }
}

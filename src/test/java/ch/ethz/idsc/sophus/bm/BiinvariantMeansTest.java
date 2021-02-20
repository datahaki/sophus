// code by jph
package ch.ethz.idsc.sophus.bm;

import ch.ethz.idsc.sophus.bm.BiinvariantMeans;
import ch.ethz.idsc.sophus.lie.so2c.So2CoveringBiinvariantMean;
import ch.ethz.idsc.sophus.usr.AssertFail;
import ch.ethz.idsc.tensor.Tensors;
import junit.framework.TestCase;

public class BiinvariantMeansTest extends TestCase {
  public void testNullFail() {
    AssertFail.of(() -> BiinvariantMeans.of(null, Tensors.vector(1)));
  }

  public void testNonAffineFail() {
    AssertFail.of(() -> BiinvariantMeans.of(So2CoveringBiinvariantMean.INSTANCE, Tensors.vector(1, 1, 1)));
  }
}

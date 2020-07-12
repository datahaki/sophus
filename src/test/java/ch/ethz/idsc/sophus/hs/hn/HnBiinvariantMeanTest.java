// code by jph
package ch.ethz.idsc.sophus.hs.hn;

import ch.ethz.idsc.sophus.hs.BiinvariantMean;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import junit.framework.TestCase;

public class HnBiinvariantMeanTest extends TestCase {
  public void testAffineFail() {
    BiinvariantMean biinvariantMean = HnBiinvariantMean.INSTANCE;
    Tensor x = HnWeierstrassCoordinate.toPoint(Tensors.vector(0, 0));
    Tensor y = HnWeierstrassCoordinate.toPoint(Tensors.vector(1, 0));
    biinvariantMean.mean(Tensors.of(x, y), Tensors.vector(0.5, 0.5));
    try {
      biinvariantMean.mean(Tensors.of(x, y), Tensors.vector(0.6, 0.5));
      fail();
    } catch (Exception exception) {
      // ---
    }
  }

  public void testNullFail() {
    try {
      HnBiinvariantMean.of(null);
      fail();
    } catch (Exception exception) {
      // ---
    }
  }
}

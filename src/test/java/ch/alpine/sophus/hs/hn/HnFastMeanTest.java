// code by jph
package ch.alpine.sophus.hs.hn;

import ch.alpine.sophus.bm.BiinvariantMean;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import junit.framework.TestCase;

public class HnFastMeanTest extends TestCase {
  public void testAffineFail() {
    BiinvariantMean biinvariantMean = HnFastMean.INSTANCE;
    Tensor x = HnWeierstrassCoordinate.toPoint(Tensors.vector(0, 0));
    Tensor y = HnWeierstrassCoordinate.toPoint(Tensors.vector(1, 0));
    biinvariantMean.mean(Tensors.of(x, y), Tensors.vector(0.5, 0.5));
    // AssertFail.of(() -> biinvariantMean.mean(Tensors.of(x, y), Tensors.vector(0.6, 0.5)));
  }
}

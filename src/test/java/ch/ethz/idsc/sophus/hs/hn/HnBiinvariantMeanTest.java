// code by jph
package ch.ethz.idsc.sophus.hs.hn;

import ch.ethz.idsc.sophus.hs.BiinvariantMean;
import ch.ethz.idsc.sophus.usr.AssertFail;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class HnBiinvariantMeanTest extends TestCase {
  public void testAffineFail() {
    BiinvariantMean biinvariantMean = HnBiinvariantMean.of(Chop._12);
    Tensor x = HnWeierstrassCoordinate.toPoint(Tensors.vector(0, 0));
    Tensor y = HnWeierstrassCoordinate.toPoint(Tensors.vector(1, 0));
    Tensor result = biinvariantMean.mean(Tensors.of(x, y), Tensors.vector(0.5, 0.5));
    Chop._10.requireClose(result, Tensors.vector(0.45508986056222733, 0, 1.09868411346781));
    AssertFail.of(() -> biinvariantMean.mean(Tensors.of(x, y), Tensors.vector(0.6, 0.5)));
  }

  public void testNullFail() {
    AssertFail.of(() -> HnBiinvariantMean.of(null));
  }
}

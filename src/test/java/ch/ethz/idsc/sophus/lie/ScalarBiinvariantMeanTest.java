// code by jph
package ch.ethz.idsc.sophus.lie;

import ch.ethz.idsc.sophus.lie.so2.So2GlobalBiinvariantMean;
import ch.ethz.idsc.sophus.lie.so2.So2LinearBiinvariantMean;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.mat.HilbertMatrix;
import junit.framework.TestCase;

public class ScalarBiinvariantMeanTest extends TestCase {
  private static final ScalarBiinvariantMean[] SO2_BIINVARIANT_MEANS = { //
      So2GlobalBiinvariantMean.INSTANCE, //
      So2LinearBiinvariantMean.INSTANCE };

  public void testEmptyFail() {
    for (ScalarBiinvariantMean so2BiinvariantMean : SO2_BIINVARIANT_MEANS)
      try {
        so2BiinvariantMean.mean(Tensors.empty(), Tensors.empty());
        fail();
      } catch (Exception exception) {
        // ---
      }
  }

  public void testMatrixFail() {
    for (ScalarBiinvariantMean so2BiinvariantMean : SO2_BIINVARIANT_MEANS)
      try {
        so2BiinvariantMean.mean(HilbertMatrix.of(3), Tensors.vector(1, 1, 1).divide(RealScalar.of(3)));
        fail();
      } catch (Exception exception) {
        // ---
      }
  }
}

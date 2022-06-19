// code by jph
package ch.alpine.sophus.lie;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.bm.ScalarBiinvariantMean;
import ch.alpine.sophus.lie.so2.So2LinearBiinvariantMean;
import ch.alpine.sophus.lie.so2.So2PhongBiinvariantMean;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.mat.HilbertMatrix;

class ScalarBiinvariantMeanTest {
  private static final ScalarBiinvariantMean[] SO2_BIINVARIANT_MEANS = { //
      So2PhongBiinvariantMean.INSTANCE, //
      So2LinearBiinvariantMean.INSTANCE };

  @Test
  void testEmptyFail() {
    for (ScalarBiinvariantMean so2BiinvariantMean : SO2_BIINVARIANT_MEANS)
      assertThrows(Exception.class, () -> so2BiinvariantMean.mean(Tensors.empty(), Tensors.empty()));
  }

  @Test
  void testMatrixFail() {
    for (ScalarBiinvariantMean so2BiinvariantMean : SO2_BIINVARIANT_MEANS)
      assertThrows(Exception.class, () -> so2BiinvariantMean.mean(HilbertMatrix.of(3), Tensors.vector(1, 1, 1).divide(RealScalar.of(3))));
  }
}

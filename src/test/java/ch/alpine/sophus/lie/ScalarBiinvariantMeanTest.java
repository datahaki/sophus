// code by jph
package ch.alpine.sophus.lie;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.lie.so2.So2LinearBiinvariantMean;
import ch.alpine.sophus.lie.so2.So2PhongBiinvariantMean;
import ch.alpine.sophus.usr.AssertFail;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.mat.HilbertMatrix;

public class ScalarBiinvariantMeanTest {
  private static final ScalarBiinvariantMean[] SO2_BIINVARIANT_MEANS = { //
      So2PhongBiinvariantMean.INSTANCE, //
      So2LinearBiinvariantMean.INSTANCE };

  @Test
  public void testEmptyFail() {
    for (ScalarBiinvariantMean so2BiinvariantMean : SO2_BIINVARIANT_MEANS)
      AssertFail.of(() -> so2BiinvariantMean.mean(Tensors.empty(), Tensors.empty()));
  }

  @Test
  public void testMatrixFail() {
    for (ScalarBiinvariantMean so2BiinvariantMean : SO2_BIINVARIANT_MEANS)
      AssertFail.of(() -> so2BiinvariantMean.mean(HilbertMatrix.of(3), Tensors.vector(1, 1, 1).divide(RealScalar.of(3))));
  }
}

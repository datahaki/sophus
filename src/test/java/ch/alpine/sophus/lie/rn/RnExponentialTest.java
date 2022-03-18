// code by jph
package ch.alpine.sophus.lie.rn;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.gbc.BarycentricCoordinate;
import ch.alpine.sophus.gbc.GbcHelper;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.mat.HilbertMatrix;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.sca.Chop;

public class RnExponentialTest {
  @Test
  public void testSimple() {
    Tensor matrix = HilbertMatrix.of(2, 3);
    assertEquals(RnExponential.INSTANCE.exp(matrix), matrix);
    assertEquals(RnExponential.INSTANCE.log(matrix), matrix);
  }

  @Test
  public void testLinearPrecision() {
    Tensor sequence = RandomVariate.of(NormalDistribution.standard(), 10, 3);
    Tensor point = RandomVariate.of(NormalDistribution.standard(), 3);
    for (BarycentricCoordinate barycentricCoordinates : GbcHelper.barycentrics(RnManifold.INSTANCE)) {
      Tensor weights = barycentricCoordinates.weights(sequence, point);
      Chop._10.requireClose(weights.dot(sequence), point);
    }
  }
}

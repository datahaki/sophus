// code by jph
package ch.ethz.idsc.sophus.lie.rn;

import ch.ethz.idsc.sophus.gbc.BarycentricCoordinate;
import ch.ethz.idsc.sophus.gbc.GbcHelper;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.mat.HilbertMatrix;
import ch.ethz.idsc.tensor.pdf.NormalDistribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class RnExponentialTest extends TestCase {
  public void testSimple() {
    Tensor matrix = HilbertMatrix.of(2, 3);
    assertEquals(RnExponential.INSTANCE.exp(matrix), matrix);
    assertEquals(RnExponential.INSTANCE.log(matrix), matrix);
  }

  public void testLinearPrecision() {
    Tensor sequence = RandomVariate.of(NormalDistribution.standard(), 10, 3);
    Tensor point = RandomVariate.of(NormalDistribution.standard(), 3);
    for (BarycentricCoordinate barycentricCoordinates : GbcHelper.barycentrics(RnManifold.INSTANCE)) {
      Tensor weights = barycentricCoordinates.weights(sequence, point);
      Chop._10.requireClose(weights.dot(sequence), point);
    }
  }
}

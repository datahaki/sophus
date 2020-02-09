// code by jph
package ch.ethz.idsc.sophus.lie.rn;

import java.io.IOException;

import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.io.Serialization;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.NormalDistribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class RnInverseDistanceCoordinatesTest extends TestCase {
  public void testSimple() throws ClassNotFoundException, IOException {
    Distribution distribution = NormalDistribution.standard();
    for (int dim = 2; dim < 5; ++dim) {
      Tensor points = RandomVariate.of(distribution, 10, dim);
      TensorUnaryOperator tensorUnaryOperator = //
          Serialization.copy(RnInverseDistanceCoordinates.of(points));
      for (int count = 0; count < 10; ++count) {
        Tensor mean = RandomVariate.of(distribution, dim);
        Tensor weights = tensorUnaryOperator.apply(mean);
        Tensor result = RnBiinvariantMean.INSTANCE.mean(points, weights);
        Chop._10.requireClose(mean, result);
      }
    }
  }

  public void testNullFail() {
    try {
      RnInverseDistanceCoordinates.of(null);
      fail();
    } catch (Exception exception) {
      // ---
    }
  }
}

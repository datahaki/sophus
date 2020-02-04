// code by jph
package ch.ethz.idsc.sophus.math.win;

import ch.ethz.idsc.sophus.lie.BiinvariantMean;
import ch.ethz.idsc.sophus.lie.se2c.Se2CoveringBiinvariantMean;
import ch.ethz.idsc.tensor.RationalScalar;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.ConstantArray;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.pdf.UniformDistribution;
import ch.ethz.idsc.tensor.red.Total;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class Se2CoveringIdfCoordinatesTest extends TestCase {
  public void testLinearReproduction() {
    Distribution distribution = UniformDistribution.unit();
    BiinvariantMean biinvariantMean = Se2CoveringBiinvariantMean.INSTANCE;
    for (int n = 5; n < 10; ++n) {
      Tensor points = RandomVariate.of(distribution, n, 3);
      TensorUnaryOperator se2CoveringIdfCoordinates = Se2CoveringIdfCoordinates.of(points);
      Tensor x = Se2CoveringBiinvariantMean.INSTANCE.mean(points, ConstantArray.of(RationalScalar.of(1, n), n));
      Tensor weights = se2CoveringIdfCoordinates.apply(x);
      Chop._10.requireClose(Total.ofVector(weights), RealScalar.ONE);
      Tensor x_recreated = biinvariantMean.mean(points, weights);
      Chop._06.requireClose(x, x_recreated);
    }
  }

  public void testRandom() {
    Distribution distribution = UniformDistribution.unit();
    BiinvariantMean biinvariantMean = Se2CoveringBiinvariantMean.INSTANCE;
    for (int n = 5; n < 10; ++n) {
      Tensor points = RandomVariate.of(distribution, n, 3);
      Tensor x = RandomVariate.of(distribution, 3);
      TensorUnaryOperator se2CoveringIdfCoordinates = Se2CoveringIdfCoordinates.of(points);
      Tensor weights = se2CoveringIdfCoordinates.apply(x);
      Chop._10.requireClose(Total.ofVector(weights), RealScalar.ONE);
      Tensor x_recreated = biinvariantMean.mean(points, weights);
      Chop._06.requireClose(x, x_recreated);
    }
  }

  public void testNullFail() {
    try {
      Se2CoveringIdfCoordinates.of(null);
      fail();
    } catch (Exception exception) {
      // ---
    }
  }
}

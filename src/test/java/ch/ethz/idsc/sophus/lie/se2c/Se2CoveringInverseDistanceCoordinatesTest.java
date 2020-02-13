// code by jph
package ch.ethz.idsc.sophus.lie.se2c;

import ch.ethz.idsc.sophus.lie.BiinvariantMean;
import ch.ethz.idsc.tensor.RationalScalar;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.ConstantArray;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.pdf.UniformDistribution;
import ch.ethz.idsc.tensor.red.Total;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class Se2CoveringInverseDistanceCoordinatesTest extends TestCase {
  public void test4Exact() {
    Distribution distribution = UniformDistribution.unit();
    int n = 4;
    for (int count = 0; count < 10; ++count) {
      Tensor points = RandomVariate.of(distribution, n, 3);
      Se2CoveringBarycenter se2CoveringBarycenter = new Se2CoveringBarycenter(points);
      Tensor xya = RandomVariate.of(distribution, 3);
      Tensor w1 = Se2CoveringInverseDistanceCoordinates.INSTANCE.weights(points, xya);
      Tensor w2 = se2CoveringBarycenter.apply(xya);
      Chop._06.requireClose(w1, w2);
    }
  }

  public void testLinearReproduction() {
    Distribution distribution = UniformDistribution.unit();
    BiinvariantMean biinvariantMean = Se2CoveringBiinvariantMean.INSTANCE;
    for (int n = 5; n < 10; ++n) {
      Tensor points = RandomVariate.of(distribution, n, 3);
      Tensor x = Se2CoveringBiinvariantMean.INSTANCE.mean(points, ConstantArray.of(RationalScalar.of(1, n), n));
      Tensor weights = Se2CoveringInverseDistanceCoordinates.INSTANCE.weights(points, x);
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
      Tensor weights = Se2CoveringInverseDistanceCoordinates.INSTANCE.weights(points, x);
      Chop._10.requireClose(Total.ofVector(weights), RealScalar.ONE);
      Tensor x_recreated = biinvariantMean.mean(points, weights);
      Chop._06.requireClose(x, x_recreated);
    }
  }

  public void testNullFail() {
    try {
      Se2CoveringInverseDistanceCoordinates.INSTANCE.weights(null, null);
      fail();
    } catch (Exception exception) {
      // ---
    }
  }
}

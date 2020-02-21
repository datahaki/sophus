// code by jph
package ch.ethz.idsc.sophus.lie.se2c;

import ch.ethz.idsc.sophus.lie.BiinvariantMean;
import ch.ethz.idsc.sophus.math.win.BarycentricCoordinate;
import ch.ethz.idsc.tensor.RationalScalar;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.ConstantArray;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.pdf.UniformDistribution;
import ch.ethz.idsc.tensor.red.Norm;
import ch.ethz.idsc.tensor.red.Total;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class Se2CoveringInverseDistanceCoordinateTest extends TestCase {
  static final BarycentricCoordinate[] BARYCENTRIC_COORDINATES = { //
      Se2CoveringInverseDistanceCoordinate.INSTANCE, //
      Se2CoveringInverseDistanceCoordinate.SQUARED, //
      Se2CoveringInverseDistanceCoordinate.INSTANCET, //
      Se2CoveringInverseDistanceCoordinate.SQUAREDT //
  };

  public void test4Exact() {
    Distribution distribution = UniformDistribution.unit();
    int n = 4;
    for (BarycentricCoordinate barycentricCoordinate : BARYCENTRIC_COORDINATES)
      for (int count = 0; count < 10; ++count) {
        Tensor points = RandomVariate.of(distribution, n, 3);
        Se2CoveringBarycenter se2CoveringBarycenter = new Se2CoveringBarycenter(points);
        Tensor xya = RandomVariate.of(distribution, 3);
        Tensor w1 = barycentricCoordinate.weights(points, xya);
        Tensor w2 = se2CoveringBarycenter.apply(xya);
        Chop._06.requireClose(w1, w2);
        Tensor mean = Se2CoveringBiinvariantMean.INSTANCE.mean(points, w1);
        Chop._06.requireClose(xya, mean);
      }
  }

  public void testLinearReproduction() {
    Distribution distribution = UniformDistribution.unit();
    BiinvariantMean biinvariantMean = Se2CoveringBiinvariantMean.INSTANCE;
    for (int n = 4; n < 10; ++n) {
      System.out.println("n=" + n);
      for (BarycentricCoordinate barycentricCoordinate : BARYCENTRIC_COORDINATES) {
        Tensor points = RandomVariate.of(distribution, n, 3);
        Tensor target = ConstantArray.of(RationalScalar.of(1, n), n);
        Tensor x = Se2CoveringBiinvariantMean.INSTANCE.mean(points, target);
        Tensor weights = barycentricCoordinate.weights(points, x);
        System.out.println(Norm._2.between(target, weights));
        // System.out.println(weights.map(Round._4));
        Chop._10.requireClose(Total.ofVector(weights), RealScalar.ONE);
        Tensor x_recreated = biinvariantMean.mean(points, weights);
        Chop._06.requireClose(x, x_recreated);
      }
    }
  }

  public void testRandom() {
    Distribution distribution = UniformDistribution.unit();
    BiinvariantMean biinvariantMean = Se2CoveringBiinvariantMean.INSTANCE;
    for (BarycentricCoordinate barycentricCoordinate : BARYCENTRIC_COORDINATES)
      for (int n = 4; n < 10; ++n) {
        Tensor points = RandomVariate.of(distribution, n, 3);
        Tensor x = RandomVariate.of(distribution, 3);
        Tensor weights = barycentricCoordinate.weights(points, x);
        Chop._10.requireClose(Total.ofVector(weights), RealScalar.ONE);
        Tensor x_recreated = biinvariantMean.mean(points, weights);
        Chop._06.requireClose(x, x_recreated);
      }
  }

  public void testNullFail() {
    for (BarycentricCoordinate barycentricCoordinate : BARYCENTRIC_COORDINATES)
      try {
        barycentricCoordinate.weights(null, null);
        fail();
      } catch (Exception exception) {
        // ---
      }
  }
}

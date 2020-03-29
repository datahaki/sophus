// code by jph
package ch.ethz.idsc.sophus.lie.se3;

import java.util.Arrays;

import ch.ethz.idsc.sophus.hs.BarycentricCoordinate;
import ch.ethz.idsc.sophus.hs.BiinvariantMeanDefect;
import ch.ethz.idsc.sophus.hs.HsBarycentricCoordinate;
import ch.ethz.idsc.sophus.hs.IterativeBiinvariantMean;
import ch.ethz.idsc.sophus.hs.MeanDefect;
import ch.ethz.idsc.sophus.math.AffineQ;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.Dimensions;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class Se3InverseDistanceCoordinatesTest extends TestCase {
  private static final IterativeBiinvariantMean ITERATIVE_BIINVARIANT_MEAN = //
      IterativeBiinvariantMean.of(Se3Manifold.HS_EXP);
  public static final MeanDefect MEAN_DEFECT = BiinvariantMeanDefect.of(Se3Manifold.HS_EXP);
  private static final BarycentricCoordinate[] BARYCENTRIC_COORDINATES = { //
      HsBarycentricCoordinate.linear(Se3Manifold.INSTANCE), //
      HsBarycentricCoordinate.smooth(Se3Manifold.INSTANCE) //
  };

  public void testRandom() {
    int fails = 0;
    for (BarycentricCoordinate barycentricCoordinate : BARYCENTRIC_COORDINATES)
      for (int count = 0; count < 10; ++count)
        for (int n = 7; n < 13; ++n) {
          Tensor sequence = Tensors.vector(i -> TestHelper.spawn_Se3(), n);
          Tensor point = TestHelper.spawn_Se3();
          try {
            Tensor weights = barycentricCoordinate.weights(sequence, point);
            AffineQ.require(weights);
            Tensor mean = ITERATIVE_BIINVARIANT_MEAN.mean(sequence, weights);
            assertEquals(Dimensions.of(mean), Arrays.asList(4, 4));
            Tensor defect = MEAN_DEFECT.defect(sequence, weights, mean);
            Chop._08.requireAllZero(defect);
          } catch (Exception exception) {
            ++fails;
          }
        }
    assertTrue(fails < 5);
  }
}

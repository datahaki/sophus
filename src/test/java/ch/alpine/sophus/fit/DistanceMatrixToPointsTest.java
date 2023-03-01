// code by jph
package ch.alpine.sophus.fit;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.Random;
import java.util.random.RandomGenerator;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.math.DistanceMatrix;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Dimensions;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.nrm.Vector2NormSquared;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.qty.Quantity;
import ch.alpine.tensor.sca.Clips;

class DistanceMatrixToPointsTest {
  @Test
  void testRankEuclidean() {
    RandomGenerator random = new Random(1);
    Distribution distribution = UniformDistribution.of(Clips.absolute(2));
    for (int d = 0; d < 4; ++d) {
      Tensor sequence = RandomVariate.of(distribution, random, 10 + d, 2 + d);
      Tensor D = DistanceMatrix.of(sequence, Vector2NormSquared::between);
      Tensor points = DistanceMatrixToPoints.of(D);
      assertEquals(Dimensions.of(points), Arrays.asList(10 + d, 2 + d));
      Tensor D2 = DistanceMatrix.of(points, Vector2NormSquared::between);
      Tolerance.CHOP.requireClose(D, D2);
    }
  }

  @Test
  void testUnit() {
    RandomGenerator random = new Random(1);
    Distribution distribution = UniformDistribution.of(Clips.absolute(2));
    for (int d = 0; d < 4; ++d) {
      Tensor sequence = RandomVariate.of(distribution, random, 8 + d, 1 + d).map(s -> Quantity.of(s, "m"));
      Tensor D = DistanceMatrix.of(sequence, Vector2NormSquared::between);
      Tensor points = DistanceMatrixToPoints.of(D);
      assertEquals(Dimensions.of(points), Arrays.asList(8 + d, 1 + d));
      Tensor D2 = DistanceMatrix.of(points, Vector2NormSquared::between);
      Tolerance.CHOP.requireClose(D, D2);
    }
  }
}

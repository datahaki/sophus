// code by jph
package ch.alpine.sophus.fit;

import java.util.Arrays;
import java.util.Random;

import ch.alpine.sophus.math.DistanceMatrix;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Dimensions;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.nrm.Vector2NormSquared;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.sca.Clips;
import junit.framework.TestCase;

public class DistanceMatrixToPointsTest extends TestCase {
  public void testRankEuclidean() {
    Random random = new Random(1);
    Distribution distribution = UniformDistribution.of(Clips.absolute(2));
    for (int d = 0; d < 4; ++d) {
      Tensor sequence = RandomVariate.of(distribution, random, 10 + d, 2 + d);
      Tensor D = DistanceMatrix.of(sequence, Vector2NormSquared::between);
      Tensor points = DistanceMatrixToPoints.points(D);
      assertEquals(Dimensions.of(points), Arrays.asList(10 + d, 2 + d));
      Tensor D2 = DistanceMatrix.of(points, Vector2NormSquared::between);
      Tolerance.CHOP.requireClose(D, D2);
    }
  }
}

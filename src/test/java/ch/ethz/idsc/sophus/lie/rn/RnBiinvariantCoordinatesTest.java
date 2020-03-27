// code by jph
package ch.ethz.idsc.sophus.lie.rn;

import ch.ethz.idsc.sophus.hs.HsBarycentricCoordinate;
import ch.ethz.idsc.sophus.hs.HsBiinvariantCoordinate;
import ch.ethz.idsc.sophus.hs.ProjectedCoordinate;
import ch.ethz.idsc.sophus.math.AffineQ;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.Array;
import ch.ethz.idsc.tensor.pdf.NormalDistribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import junit.framework.TestCase;

public class RnBiinvariantCoordinatesTest extends TestCase {
  private static final ProjectedCoordinate[] PROJECTED_COORDINATES = { //
      HsBarycentricCoordinate.linear(RnManifold.INSTANCE), //
      HsBarycentricCoordinate.smooth(RnManifold.INSTANCE), //
      HsBiinvariantCoordinate.affine(RnManifold.INSTANCE), //
  };

  public void testColinear() {
    int d = 2;
    int n = 5;
    for (ProjectedCoordinate projectedCoordinate : PROJECTED_COORDINATES) {
      Tensor sequence = RandomVariate.of(NormalDistribution.standard(), n, d);
      sequence.append(sequence.get(n - 1).multiply(RealScalar.of(5)));
      Tensor weights = projectedCoordinate.weights(sequence, Array.zeros(d));
      assertEquals(sequence.length(), n + 1);
      AffineQ.require(weights);
    }
  }
}

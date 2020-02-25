// code by jph
package ch.ethz.idsc.sophus.lie.se2c;

import ch.ethz.idsc.sophus.math.AffineQ;
import ch.ethz.idsc.sophus.math.win.BarycentricCoordinate;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.UnitVector;
import ch.ethz.idsc.tensor.mat.Tolerance;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.NormalDistribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import junit.framework.TestCase;

public class Se2CoveringBiinvariantCoordinateTest extends TestCase {
  private static final BarycentricCoordinate[] BARYCENTRIC_COORDINATES = { //
      Se2CoveringBiinvariantCoordinate.INSTANCE, //
      Se2CoveringInverseDistanceCoordinate.SQUARED //
  };

  public void testLagrange() {
    Distribution distribution = NormalDistribution.standard();
    for (int n = 4; n < 10; ++n) {
      System.out.println(n);
      Tensor sequence = RandomVariate.of(distribution, n, 3);
      for (BarycentricCoordinate barycentricCoordinate : BARYCENTRIC_COORDINATES) {
        for (int index = 0; index < n; ++index) {
          Tensor weights = barycentricCoordinate.weights(sequence, sequence.get(index));
          AffineQ.require(weights);
          System.out.println(weights);
          Tolerance.CHOP.requireClose(weights, UnitVector.of(n, index));
        }
      }
    }
  }
}

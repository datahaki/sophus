// code by jph
package ch.ethz.idsc.sophus.gbc;

import ch.ethz.idsc.sophus.lie.rn.RnManifold;
import ch.ethz.idsc.sophus.math.AffineQ;
import ch.ethz.idsc.sophus.ply.d2.Barycenter;
import ch.ethz.idsc.sophus.ply.d2.ThreePointCoordinate;
import ch.ethz.idsc.tensor.DeterminateScalarQ;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.Array;
import ch.ethz.idsc.tensor.lie.r2.CirclePoints;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class HsCoordinatesTest extends TestCase {
  public void testSimple() {
    for (Barycenter barycenter : Barycenter.values()) {
      BarycentricCoordinate barycentricCoordinate = HsCoordinates.wrap(RnManifold.INSTANCE, ThreePointCoordinate.of(barycenter));
      for (int n = 3; n < 10; ++n) {
        Tensor w1 = barycentricCoordinate.weights(CirclePoints.of(n), Array.zeros(2));
        Chop._08.requireClose(w1, AveragingWeights.of(n));
        AffineQ.require(w1, Chop._08);
        Tensor w2 = barycentricCoordinate.weights(CirclePoints.of(n), Tensors.vector(2, 2));
        assertEquals(w2.length(), n);
        assertTrue(w2.stream().map(Scalar.class::cast).allMatch(DeterminateScalarQ::of));
        AffineQ.require(w2, Chop._08);
      }
    }
  }
}

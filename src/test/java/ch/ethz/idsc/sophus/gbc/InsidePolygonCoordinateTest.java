// code by jph
package ch.ethz.idsc.sophus.gbc;

import ch.ethz.idsc.sophus.lie.r2.Barycenter;
import ch.ethz.idsc.sophus.lie.rn.RnManifold;
import ch.ethz.idsc.sophus.lie.so2.CirclePoints;
import ch.ethz.idsc.sophus.math.AffineQ;
import ch.ethz.idsc.tensor.DoubleScalar;
import ch.ethz.idsc.tensor.RationalScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.Array;
import ch.ethz.idsc.tensor.alg.ConstantArray;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class InsidePolygonCoordinateTest extends TestCase {
  public void testSimple() {
    for (Barycenter barycenter : Barycenter.values()) {
      BarycentricCoordinate barycentricCoordinate = InsidePolygonCoordinate.of(RnManifold.INSTANCE, barycenter);
      for (int n = 3; n < 10; ++n) {
        Tensor w1 = barycentricCoordinate.weights(CirclePoints.of(n), Array.zeros(2));
        Chop._08.requireClose(w1, ConstantArray.of(RationalScalar.of(1, n), n));
        AffineQ.require(w1);
        Tensor w2 = barycentricCoordinate.weights(CirclePoints.of(n), Tensors.vector(2, 2));
        assertEquals(w2.toString(), ConstantArray.of(DoubleScalar.INDETERMINATE, n).toString());
      }
    }
  }
}
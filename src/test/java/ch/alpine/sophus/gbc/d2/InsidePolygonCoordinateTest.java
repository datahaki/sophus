// code by jph
package ch.alpine.sophus.gbc.d2;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.gbc.AveragingWeights;
import ch.alpine.sophus.gbc.BarycentricCoordinate;
import ch.alpine.sophus.gbc.HsCoordinates;
import ch.alpine.sophus.lie.rn.RnManifold;
import ch.alpine.sophus.math.AffineQ;
import ch.alpine.tensor.DoubleScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Array;
import ch.alpine.tensor.alg.ConstantArray;
import ch.alpine.tensor.lie.r2.CirclePoints;
import ch.alpine.tensor.sca.Chop;

public class InsidePolygonCoordinateTest {
  @Test
  public void testSimple() {
    for (Barycenter barycenter : Barycenter.values()) {
      BarycentricCoordinate barycentricCoordinate = //
          HsCoordinates.wrap(RnManifold.INSTANCE, new InsidePolygonCoordinate(ThreePointCoordinate.of(barycenter)));
      for (int n = 3; n < 10; ++n) {
        Tensor w1 = barycentricCoordinate.weights(CirclePoints.of(n), Array.zeros(2));
        Chop._08.requireClose(w1, AveragingWeights.of(n));
        AffineQ.require(w1, Chop._08);
        Tensor w2 = barycentricCoordinate.weights(CirclePoints.of(n), Tensors.vector(2, 2));
        assertEquals(w2.toString(), ConstantArray.of(DoubleScalar.INDETERMINATE, n).toString());
      }
    }
  }
}

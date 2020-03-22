// code by jph
package ch.ethz.idsc.sophus.math.win;

import java.io.IOException;

import ch.ethz.idsc.sophus.lie.rn.RnMetric;
import ch.ethz.idsc.sophus.lie.rn.RnMetricSquared;
import ch.ethz.idsc.tensor.ExactTensorQ;
import ch.ethz.idsc.tensor.RationalScalar;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.UnitVector;
import ch.ethz.idsc.tensor.io.Serialization;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.pdf.UniformDistribution;
import ch.ethz.idsc.tensor.qty.Quantity;
import ch.ethz.idsc.tensor.red.Norm2Squared;
import ch.ethz.idsc.tensor.red.Total;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class InverseDistanceWeightingTest extends TestCase {
  public void testSimple() {
    BarycentricCoordinate barycentricCoordinate = //
        InverseDistanceWeighting.of(RnMetricSquared.INSTANCE);
    Tensor weights = barycentricCoordinate.weights(Tensors.vector(1, 3).map(Tensors::of), RealScalar.of(2).map(Tensors::of));
    assertEquals(weights, Tensors.of(RationalScalar.HALF, RationalScalar.HALF));
  }

  public void testExact() {
    BarycentricCoordinate barycentricCoordinate = InverseDistanceWeighting.of(RnMetricSquared.INSTANCE);
    Tensor weights = barycentricCoordinate.weights(Tensors.fromString("{{2}, {3}}"), Tensors.vector(3));
    ExactTensorQ.require(weights);
    assertEquals(weights, UnitVector.of(2, 1));
  }

  public void testPoints() {
    Distribution distribution = UniformDistribution.unit();
    for (int n = 5; n < 10; ++n) {
      Tensor p1 = RandomVariate.of(distribution, n, 2);
      BarycentricCoordinate barycentricCoordinate = //
          InverseDistanceWeighting.of(RnMetricSquared.INSTANCE);
      for (int index = 0; index < p1.length(); ++index) {
        Tensor q = barycentricCoordinate.weights(p1, p1.get(index));
        Chop._10.requireClose(q, UnitVector.of(n, index));
      }
    }
  }

  public void testQuantity() throws ClassNotFoundException, IOException {
    Distribution distribution = UniformDistribution.of(Quantity.of(-1, "m"), Quantity.of(+1, "m"));
    for (int d = 2; d < 6; ++d)
      for (int n = d + 1; n < 10; ++n) {
        Tensor points = RandomVariate.of(distribution, n, d);
        Tensor x = RandomVariate.of(distribution, d);
        BarycentricCoordinate barycentricCoordinate = //
            Serialization.copy(InverseDistanceWeighting.of(RnMetric.INSTANCE));
        Tensor weights = barycentricCoordinate.weights(points, x);
        Chop._10.requireClose(Total.ofVector(weights), RealScalar.ONE);
      }
  }

  public void testFailMetricNull() {
    try {
      InverseDistanceWeighting.of(null);
      fail();
    } catch (Exception exception) {
      // ---
    }
  }

  public void testFailPointsNull() {
    try {
      InverseDistanceWeighting.of(Norm2Squared::between).weights(null, null);
      fail();
    } catch (Exception exception) {
      // ---
    }
  }
}

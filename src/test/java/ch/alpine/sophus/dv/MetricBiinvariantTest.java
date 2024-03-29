// code by jph
package ch.alpine.sophus.dv;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.hs.Genesis;
import ch.alpine.sophus.hs.HsDesign;
import ch.alpine.sophus.hs.Sedarim;
import ch.alpine.sophus.lie.rn.RnGroup;
import ch.alpine.sophus.math.var.InversePowerVariogram;
import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.UnitVector;
import ch.alpine.tensor.chq.ExactTensorQ;
import ch.alpine.tensor.ext.Serialization;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.qty.Quantity;
import ch.alpine.tensor.red.Total;
import ch.alpine.tensor.sca.Chop;

class MetricBiinvariantTest {
  private static final Genesis INVERSE_DISTANCE_WEIGHTING = //
      new MetricBiinvariant(RnGroup.INSTANCE).weighting(InversePowerVariogram.of(2));

  @Test
  void testSimple() {
    // inverseDistanceWeighting
    BarycentricCoordinate barycentricCoordinate = //
        new HsCoordinates(new HsDesign(RnGroup.INSTANCE), INVERSE_DISTANCE_WEIGHTING);
    Tensor weights = barycentricCoordinate.weights(Tensors.vector(1, 3).map(Tensors::of), RealScalar.of(2).map(Tensors::of));
    assertEquals(weights, Tensors.of(RationalScalar.HALF, RationalScalar.HALF));
  }

  @Test
  void testExact() {
    BarycentricCoordinate barycentricCoordinate = //
        new HsCoordinates(new HsDesign(RnGroup.INSTANCE), INVERSE_DISTANCE_WEIGHTING);
    Tensor weights = barycentricCoordinate.weights(Tensors.fromString("{{2}, {3}}"), Tensors.vector(3));
    ExactTensorQ.require(weights);
    assertEquals(weights, UnitVector.of(2, 1));
  }

  @Test
  void testPoints() {
    Distribution distribution = UniformDistribution.unit();
    BarycentricCoordinate barycentricCoordinate = //
        new HsCoordinates(new HsDesign(RnGroup.INSTANCE), INVERSE_DISTANCE_WEIGHTING);
    for (int n = 5; n < 10; ++n) {
      Tensor p1 = RandomVariate.of(distribution, n, 2);
      for (int index = 0; index < p1.length(); ++index) {
        Tensor q = barycentricCoordinate.weights(p1, p1.get(index));
        Chop._10.requireClose(q, UnitVector.of(n, index));
      }
    }
  }

  @Test
  void testQuantity() throws ClassNotFoundException, IOException {
    Distribution distribution = UniformDistribution.of(Quantity.of(-1, "m"), Quantity.of(+1, "m"));
    BarycentricCoordinate barycentricCoordinate = Serialization.copy( //
        new HsCoordinates(new HsDesign(RnGroup.INSTANCE), //
            new MetricBiinvariant(RnGroup.INSTANCE).weighting(InversePowerVariogram.of(1))));
    Biinvariant biinvariant = Biinvariants.METRIC.ofSafe(RnGroup.INSTANCE);
    for (int d = 2; d < 6; ++d)
      for (int n = d + 1; n < 10; ++n) {
        Tensor points = RandomVariate.of(distribution, n, d);
        Sedarim shw = biinvariant.weighting(InversePowerVariogram.of(1), points);
        Tensor x = RandomVariate.of(distribution, d);
        Tensor weights1 = barycentricCoordinate.weights(points, x);
        Chop._10.requireClose(Total.ofVector(weights1), RealScalar.ONE);
        Tensor weights2 = shw.sunder(x);
        Chop._10.requireClose(weights1, weights2);
      }
  }

  @Test
  void testFail() {
    assertThrows(Exception.class, () -> new MetricBiinvariant(RnGroup.INSTANCE).weighting(null));
    assertThrows(Exception.class, () -> new MetricBiinvariant(RnGroup.INSTANCE).coordinate(null));
  }
}

// code by jph
package ch.alpine.sophus.itp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.gbc.BarycentricCoordinate;
import ch.alpine.sophus.gbc.HsCoordinates;
import ch.alpine.sophus.hs.MetricBiinvariant;
import ch.alpine.sophus.lie.rn.RnManifold;
import ch.alpine.sophus.math.var.InversePowerVariogram;
import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.UnitVector;
import ch.alpine.tensor.api.TensorUnaryOperator;
import ch.alpine.tensor.chq.ExactTensorQ;
import ch.alpine.tensor.ext.Serialization;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.qty.Quantity;
import ch.alpine.tensor.red.Total;
import ch.alpine.tensor.sca.Chop;

public class InverseDistanceWeightingTest {
  @Test
  public void testSimple() {
    BarycentricCoordinate barycentricCoordinate = //
        HsCoordinates.wrap(RnManifold.INSTANCE, new InverseDistanceWeighting(InversePowerVariogram.of(2)));
    Tensor weights = barycentricCoordinate.weights(Tensors.vector(1, 3).map(Tensors::of), RealScalar.of(2).map(Tensors::of));
    assertEquals(weights, Tensors.of(RationalScalar.HALF, RationalScalar.HALF));
  }

  @Test
  public void testExact() {
    BarycentricCoordinate barycentricCoordinate = //
        HsCoordinates.wrap(RnManifold.INSTANCE, new InverseDistanceWeighting(InversePowerVariogram.of(2)));
    Tensor weights = barycentricCoordinate.weights(Tensors.fromString("{{2}, {3}}"), Tensors.vector(3));
    ExactTensorQ.require(weights);
    assertEquals(weights, UnitVector.of(2, 1));
  }

  @Test
  public void testPoints() {
    Distribution distribution = UniformDistribution.unit();
    BarycentricCoordinate barycentricCoordinate = //
        HsCoordinates.wrap(RnManifold.INSTANCE, new InverseDistanceWeighting(InversePowerVariogram.of(2)));
    for (int n = 5; n < 10; ++n) {
      Tensor p1 = RandomVariate.of(distribution, n, 2);
      for (int index = 0; index < p1.length(); ++index) {
        Tensor q = barycentricCoordinate.weights(p1, p1.get(index));
        Chop._10.requireClose(q, UnitVector.of(n, index));
      }
    }
  }

  @Test
  public void testQuantity() throws ClassNotFoundException, IOException {
    Distribution distribution = UniformDistribution.of(Quantity.of(-1, "m"), Quantity.of(+1, "m"));
    BarycentricCoordinate barycentricCoordinate = //
        Serialization.copy(HsCoordinates.wrap(RnManifold.INSTANCE, new InverseDistanceWeighting(InversePowerVariogram.of(1))));
    for (int d = 2; d < 6; ++d)
      for (int n = d + 1; n < 10; ++n) {
        Tensor points = RandomVariate.of(distribution, n, d);
        TensorUnaryOperator shw = MetricBiinvariant.EUCLIDEAN.weighting(RnManifold.INSTANCE, InversePowerVariogram.of(1), points);
        Tensor x = RandomVariate.of(distribution, d);
        Tensor weights1 = barycentricCoordinate.weights(points, x);
        Chop._10.requireClose(Total.ofVector(weights1), RealScalar.ONE);
        Tensor weights2 = shw.apply(x);
        Chop._10.requireClose(weights1, weights2);
      }
  }

  @Test
  public void testFail() {
    assertThrows(Exception.class, () -> new InverseDistanceWeighting(null));
  }
}

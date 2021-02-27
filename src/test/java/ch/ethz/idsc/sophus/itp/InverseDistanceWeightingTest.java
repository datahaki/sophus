// code by jph
package ch.ethz.idsc.sophus.itp;

import java.io.IOException;

import ch.ethz.idsc.sophus.gbc.HsCoordinates;
import ch.ethz.idsc.sophus.hs.MetricBiinvariant;
import ch.ethz.idsc.sophus.lie.rn.RnManifold;
import ch.ethz.idsc.sophus.math.WeightingInterface;
import ch.ethz.idsc.sophus.math.var.InversePowerVariogram;
import ch.ethz.idsc.tensor.ExactTensorQ;
import ch.ethz.idsc.tensor.RationalScalar;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.UnitVector;
import ch.ethz.idsc.tensor.api.TensorUnaryOperator;
import ch.ethz.idsc.tensor.ext.Serialization;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.pdf.UniformDistribution;
import ch.ethz.idsc.tensor.qty.Quantity;
import ch.ethz.idsc.tensor.red.Total;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class InverseDistanceWeightingTest extends TestCase {
  public void testSimple() {
    WeightingInterface weightingInterface = //
        HsCoordinates.wrap(RnManifold.INSTANCE, InverseDistanceWeighting.of(InversePowerVariogram.of(2)));
    Tensor weights = weightingInterface.weights(Tensors.vector(1, 3).map(Tensors::of), RealScalar.of(2).map(Tensors::of));
    assertEquals(weights, Tensors.of(RationalScalar.HALF, RationalScalar.HALF));
  }

  public void testExact() {
    WeightingInterface weightingInterface = //
        HsCoordinates.wrap(RnManifold.INSTANCE, InverseDistanceWeighting.of(InversePowerVariogram.of(2)));
    Tensor weights = weightingInterface.weights(Tensors.fromString("{{2}, {3}}"), Tensors.vector(3));
    ExactTensorQ.require(weights);
    assertEquals(weights, UnitVector.of(2, 1));
  }

  public void testPoints() {
    Distribution distribution = UniformDistribution.unit();
    WeightingInterface weightingInterface = //
        HsCoordinates.wrap(RnManifold.INSTANCE, InverseDistanceWeighting.of(InversePowerVariogram.of(2)));
    for (int n = 5; n < 10; ++n) {
      Tensor p1 = RandomVariate.of(distribution, n, 2);
      for (int index = 0; index < p1.length(); ++index) {
        Tensor q = weightingInterface.weights(p1, p1.get(index));
        Chop._10.requireClose(q, UnitVector.of(n, index));
      }
    }
  }

  public void testQuantity() throws ClassNotFoundException, IOException {
    Distribution distribution = UniformDistribution.of(Quantity.of(-1, "m"), Quantity.of(+1, "m"));
    WeightingInterface weightingInterface = //
        Serialization.copy(HsCoordinates.wrap(RnManifold.INSTANCE, InverseDistanceWeighting.of(InversePowerVariogram.of(1))));
    for (int d = 2; d < 6; ++d)
      for (int n = d + 1; n < 10; ++n) {
        Tensor points = RandomVariate.of(distribution, n, d);
        TensorUnaryOperator shw = MetricBiinvariant.EUCLIDEAN.weighting(RnManifold.INSTANCE, InversePowerVariogram.of(1), points);
        Tensor x = RandomVariate.of(distribution, d);
        Tensor weights1 = weightingInterface.weights(points, x);
        Chop._10.requireClose(Total.ofVector(weights1), RealScalar.ONE);
        Tensor weights2 = shw.apply(x);
        Chop._10.requireClose(weights1, weights2);
      }
  }
}

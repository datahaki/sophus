// code by jph
package ch.ethz.idsc.sophus.math.win;

import java.io.IOException;

import ch.ethz.idsc.sophus.lie.rn.RnMetric;
import ch.ethz.idsc.tensor.RationalScalar;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.UnitVector;
import ch.ethz.idsc.tensor.io.Serialization;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;
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
    InverseDistanceWeighting inverseDistance = new InverseDistanceWeighting(Norm2Squared::between);
    Tensor weights = inverseDistance.of(Tensors.vector(1, 3).map(Tensors::of)).apply(RealScalar.of(2).map(Tensors::of));
    assertEquals(weights, Tensors.of(RationalScalar.HALF, RationalScalar.HALF));
  }

  public void testPoints() {
    Distribution distribution = UniformDistribution.unit();
    for (int n = 5; n < 10; ++n) {
      Tensor p1 = RandomVariate.of(distribution, n, 2);
      TensorUnaryOperator inverseDistance = new InverseDistanceWeighting(Norm2Squared::between).of(p1);
      for (int index = 0; index < p1.length(); ++index) {
        Tensor q = inverseDistance.apply(p1.get(index));
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
        TensorUnaryOperator tensorUnaryOperator = //
            Serialization.copy(new InverseDistanceWeighting(RnMetric.INSTANCE).of(points));
        Tensor weights = tensorUnaryOperator.apply(x);
        Chop._10.requireClose(Total.ofVector(weights), RealScalar.ONE);
      }
  }

  public void testFailMetricNull() {
    try {
      new InverseDistanceWeighting(null);
      fail();
    } catch (Exception exception) {
      // ---
    }
  }

  public void testFailPointsNull() {
    try {
      new InverseDistanceWeighting(Norm2Squared::between).of(null);
      fail();
    } catch (Exception exception) {
      // ---
    }
  }
}

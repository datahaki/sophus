// code by jph
package ch.ethz.idsc.sophus.krg;

import java.io.IOException;

import ch.ethz.idsc.sophus.hs.sn.SnPhongMean;
import ch.ethz.idsc.sophus.hs.sn.SnRandomSample;
import ch.ethz.idsc.sophus.itp.CrossAveraging;
import ch.ethz.idsc.sophus.lie.rn.RnManifold;
import ch.ethz.idsc.sophus.lie.rn.RnMetric;
import ch.ethz.idsc.sophus.lie.rn.RnMetricSquared;
import ch.ethz.idsc.sophus.lie.se2c.Se2CoveringManifold;
import ch.ethz.idsc.sophus.math.WeightingInterface;
import ch.ethz.idsc.sophus.math.sample.RandomSample;
import ch.ethz.idsc.sophus.math.sample.RandomSampleInterface;
import ch.ethz.idsc.tensor.ExactTensorQ;
import ch.ethz.idsc.tensor.RationalScalar;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.UnitVector;
import ch.ethz.idsc.tensor.alg.VectorQ;
import ch.ethz.idsc.tensor.io.Serialization;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.NormalDistribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.pdf.UniformDistribution;
import ch.ethz.idsc.tensor.qty.Quantity;
import ch.ethz.idsc.tensor.red.Norm2Squared;
import ch.ethz.idsc.tensor.red.Total;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class ShepardWeightingTest extends TestCase {
  public void testSimplePD() throws ClassNotFoundException, IOException {
    for (PseudoDistances pseudoDistances : PseudoDistances.values()) {
      WeightingInterface shepardInterpolation = Serialization.copy(ShepardWeighting.of( //
          pseudoDistances.create(Se2CoveringManifold.INSTANCE, InversePowerVariogram.of(2))));
      Distribution distribution = NormalDistribution.standard();
      for (int n = 10; n < 20; ++n) {
        Tensor sequence = RandomVariate.of(distribution, n, 3);
        RandomSampleInterface randomSampleInterface = SnRandomSample.of(4);
        Tensor values = RandomSample.of(randomSampleInterface, n);
        Tensor point = RandomVariate.of(distribution, 3);
        Tensor evaluate = CrossAveraging.of(p -> shepardInterpolation.weights(sequence, p), SnPhongMean.INSTANCE, values).apply(point);
        VectorQ.requireLength(evaluate, 5);
      }
    }
  }

  public void testSimple() {
    WeightingInterface weightingInterface = InverseDistanceWeighting.of(RnMetricSquared.INSTANCE);
    Tensor weights = weightingInterface.weights(Tensors.vector(1, 3).map(Tensors::of), RealScalar.of(2).map(Tensors::of));
    assertEquals(weights, Tensors.of(RationalScalar.HALF, RationalScalar.HALF));
  }

  public void testExact() {
    WeightingInterface weightingInterface = InverseDistanceWeighting.of(RnMetricSquared.INSTANCE);
    Tensor weights = weightingInterface.weights(Tensors.fromString("{{2}, {3}}"), Tensors.vector(3));
    ExactTensorQ.require(weights);
    assertEquals(weights, UnitVector.of(2, 1));
  }

  public void testPoints() {
    Distribution distribution = UniformDistribution.unit();
    for (int n = 5; n < 10; ++n) {
      Tensor p1 = RandomVariate.of(distribution, n, 2);
      WeightingInterface weightingInterface = InverseDistanceWeighting.of(RnMetricSquared.INSTANCE);
      for (int index = 0; index < p1.length(); ++index) {
        Tensor q = weightingInterface.weights(p1, p1.get(index));
        Chop._10.requireClose(q, UnitVector.of(n, index));
      }
    }
  }

  public void testQuantity() throws ClassNotFoundException, IOException {
    Distribution distribution = UniformDistribution.of(Quantity.of(-1, "m"), Quantity.of(+1, "m"));
    WeightingInterface shw = ShepardWeighting.absolute(RnManifold.INSTANCE, InversePowerVariogram.of(1));
    for (int d = 2; d < 6; ++d)
      for (int n = d + 1; n < 10; ++n) {
        Tensor points = RandomVariate.of(distribution, n, d);
        Tensor x = RandomVariate.of(distribution, d);
        WeightingInterface weightingInterface = Serialization.copy(InverseDistanceWeighting.of(RnMetric.INSTANCE));
        Tensor weights1 = weightingInterface.weights(points, x);
        Chop._10.requireClose(Total.ofVector(weights1), RealScalar.ONE);
        Tensor weights2 = shw.weights(points, x);
        Chop._10.requireClose(weights1, weights2);
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

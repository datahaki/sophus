// code by jph
package ch.ethz.idsc.sophus.dv;

import ch.ethz.idsc.sophus.gbc.HsCoordinates;
import ch.ethz.idsc.sophus.hs.Biinvariants;
import ch.ethz.idsc.sophus.hs.VectorLogManifold;
import ch.ethz.idsc.sophus.hs.sn.SnManifold;
import ch.ethz.idsc.sophus.hs.sn.SnRandomSample;
import ch.ethz.idsc.sophus.lie.rn.RnManifold;
import ch.ethz.idsc.sophus.lie.se2.Se2Manifold;
import ch.ethz.idsc.sophus.lie.se2c.Se2CoveringManifold;
import ch.ethz.idsc.sophus.math.WeightingInterface;
import ch.ethz.idsc.sophus.math.sample.RandomSample;
import ch.ethz.idsc.sophus.math.sample.RandomSampleInterface;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.api.TensorUnaryOperator;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.pdf.UniformDistribution;
import ch.ethz.idsc.tensor.sca.Clips;
import junit.framework.TestCase;

/** anchor == target */
public class LeveragesDistanceVectorTest extends TestCase {
  public void testRn() {
    Tensor sequence = RandomVariate.of(UniformDistribution.unit(), 10, 3);
    VectorLogManifold vectorLogManifold = RnManifold.INSTANCE;
    TensorUnaryOperator w2 = Biinvariants.LEVERAGES.distances(vectorLogManifold, sequence);
    for (int count = 0; count < 10; ++count) {
      Tensor point = RandomVariate.of(UniformDistribution.unit(), 3);
      w2.apply(point);
    }
  }

  public void testSn() {
    RandomSampleInterface randomSampleInterface = SnRandomSample.of(2);
    Tensor sequence = RandomSample.of(randomSampleInterface, 10);
    VectorLogManifold vectorLogManifold = SnManifold.INSTANCE;
    TensorUnaryOperator w2 = Biinvariants.LEVERAGES.distances(vectorLogManifold, sequence);
    for (int count = 0; count < 10; ++count) {
      Tensor point = RandomSample.of(randomSampleInterface);
      w2.apply(point);
    }
  }

  public void testSe2() {
    Distribution distribution = UniformDistribution.unit();
    Tensor sequence = RandomVariate.of(distribution, 10, 3);
    VectorLogManifold vectorLogManifold = Se2Manifold.INSTANCE;
    TensorUnaryOperator w2 = Biinvariants.LEVERAGES.distances(vectorLogManifold, sequence);
    for (int count = 0; count < 10; ++count) {
      Tensor point = RandomVariate.of(distribution, 3);
      w2.apply(point);
    }
  }

  public void testDistances() {
    Distribution distribution = UniformDistribution.of(Clips.absolute(10));
    VectorLogManifold vectorLogManifold = Se2CoveringManifold.INSTANCE;
    WeightingInterface w1 = HsCoordinates.wrap(vectorLogManifold, LeveragesDistanceVector.INSTANCE);
    for (int length = 4; length < 10; ++length) {
      Tensor sequence = RandomVariate.of(distribution, length, 3);
      Tensor point = RandomVariate.of(distribution, 3);
      w1.weights(sequence, point);
    }
  }

  public void testSimple() {
    VectorLogManifold vectorLogManifold = Se2CoveringManifold.INSTANCE;
    Distribution distribution = UniformDistribution.of(Clips.absolute(10));
    for (int length = 4; length < 10; ++length) {
      Tensor sequence = RandomVariate.of(distribution, length, 3);
      Tensor point = RandomVariate.of(distribution, 3);
      WeightingInterface weightingInterface = HsCoordinates.wrap(vectorLogManifold, LeveragesDistanceVector.INSTANCE);
      weightingInterface.weights(sequence, point);
    }
  }
}

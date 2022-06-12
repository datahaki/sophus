// code by jph
package ch.alpine.sophus.dv;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.gbc.BarycentricCoordinate;
import ch.alpine.sophus.gbc.HsCoordinates;
import ch.alpine.sophus.hs.Biinvariants;
import ch.alpine.sophus.hs.Manifold;
import ch.alpine.sophus.hs.sn.SnManifold;
import ch.alpine.sophus.hs.sn.SnRandomSample;
import ch.alpine.sophus.lie.rn.RnGroup;
import ch.alpine.sophus.lie.se2.Se2Group;
import ch.alpine.sophus.lie.se2c.Se2CoveringGroup;
import ch.alpine.sophus.math.sample.RandomSample;
import ch.alpine.sophus.math.sample.RandomSampleInterface;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.TensorUnaryOperator;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.sca.Clips;

/** anchor == target */
class LeveragesDistanceVectorTest {
  @Test
  public void testRn() {
    Tensor sequence = RandomVariate.of(UniformDistribution.unit(), 10, 3);
    Manifold manifold = RnGroup.INSTANCE;
    TensorUnaryOperator w2 = Biinvariants.LEVERAGES.distances(manifold, sequence);
    for (int count = 0; count < 10; ++count) {
      Tensor point = RandomVariate.of(UniformDistribution.unit(), 3);
      w2.apply(point);
    }
  }

  @Test
  public void testSn() {
    RandomSampleInterface randomSampleInterface = SnRandomSample.of(2);
    Tensor sequence = RandomSample.of(randomSampleInterface, 10);
    Manifold manifold = SnManifold.INSTANCE;
    TensorUnaryOperator w2 = Biinvariants.LEVERAGES.distances(manifold, sequence);
    for (int count = 0; count < 10; ++count) {
      Tensor point = RandomSample.of(randomSampleInterface);
      w2.apply(point);
    }
  }

  @Test
  public void testSe2() {
    Distribution distribution = UniformDistribution.unit();
    Tensor sequence = RandomVariate.of(distribution, 10, 3);
    Manifold manifold = Se2Group.INSTANCE;
    TensorUnaryOperator w2 = Biinvariants.LEVERAGES.distances(manifold, sequence);
    for (int count = 0; count < 10; ++count) {
      Tensor point = RandomVariate.of(distribution, 3);
      w2.apply(point);
    }
  }

  @Test
  public void testDistances() {
    Distribution distribution = UniformDistribution.of(Clips.absolute(10));
    Manifold manifold = Se2CoveringGroup.INSTANCE;
    BarycentricCoordinate w1 = HsCoordinates.of(manifold, LeveragesDistanceVector.INSTANCE);
    for (int length = 4; length < 10; ++length) {
      Tensor sequence = RandomVariate.of(distribution, length, 3);
      Tensor point = RandomVariate.of(distribution, 3);
      w1.weights(sequence, point);
    }
  }

  @Test
  public void testSimple() {
    Manifold manifold = Se2CoveringGroup.INSTANCE;
    Distribution distribution = UniformDistribution.of(Clips.absolute(10));
    for (int length = 4; length < 10; ++length) {
      Tensor sequence = RandomVariate.of(distribution, length, 3);
      Tensor point = RandomVariate.of(distribution, 3);
      BarycentricCoordinate barycentricCoordinate = HsCoordinates.of(manifold, LeveragesDistanceVector.INSTANCE);
      barycentricCoordinate.weights(sequence, point);
    }
  }
}

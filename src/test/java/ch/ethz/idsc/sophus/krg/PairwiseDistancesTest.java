// code by jph
package ch.ethz.idsc.sophus.krg;

import ch.ethz.idsc.sophus.hs.VectorLogManifold;
import ch.ethz.idsc.sophus.hs.sn.SnManifold;
import ch.ethz.idsc.sophus.hs.sn.SnRandomSample;
import ch.ethz.idsc.sophus.lie.rn.RnManifold;
import ch.ethz.idsc.sophus.lie.se2c.Se2CoveringManifold;
import ch.ethz.idsc.sophus.math.sample.RandomSample;
import ch.ethz.idsc.sophus.math.sample.RandomSampleInterface;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.pdf.UniformDistribution;
import ch.ethz.idsc.tensor.sca.Chop;
import ch.ethz.idsc.tensor.sca.Clips;
import ch.ethz.idsc.tensor.sca.ScalarUnaryOperator;
import junit.framework.TestCase;

public class PairwiseDistancesTest extends TestCase {
  public void testRn() {
    Distribution distribution = UniformDistribution.of(Clips.absolute(10));
    ScalarUnaryOperator variogram = s -> s;
    VectorLogManifold vectorLogManifold = RnManifold.INSTANCE;
    for (int length = 4; length < 10; ++length) {
      Tensor sequence = RandomVariate.of(distribution, length, 3);
      Tensor point = RandomVariate.of(distribution, 3);
      PairwiseDistances d1 = PairwiseDistances.frobenius(vectorLogManifold, variogram, sequence);
      PairwiseDistances d2 = PairwiseDistances.norm2(vectorLogManifold, variogram, sequence);
      BiinvariantVector v1 = d1.biinvariantVector(point);
      BiinvariantVector v2 = d2.biinvariantVector(point);
      Chop._10.requireClose(v1.normalized(), v2.normalized());
    }
  }

  public void testSn() {
    RandomSampleInterface randomSampleInterface = SnRandomSample.of(2);
    ScalarUnaryOperator variogram = s -> s;
    VectorLogManifold vectorLogManifold = SnManifold.INSTANCE;
    for (int length = 4; length < 10; ++length) {
      Tensor sequence = RandomSample.of(randomSampleInterface, length);
      Tensor point = RandomSample.of(randomSampleInterface);
      PairwiseDistances d1 = PairwiseDistances.frobenius(vectorLogManifold, variogram, sequence);
      PairwiseDistances d2 = PairwiseDistances.norm2(vectorLogManifold, variogram, sequence);
      BiinvariantVector v1 = d1.biinvariantVector(point);
      BiinvariantVector v2 = d2.biinvariantVector(point);
      Chop._01.requireClose(v1.normalized(), v2.normalized());
    }
  }

  public void testSe2C() {
    Distribution distribution = UniformDistribution.of(Clips.absolute(10));
    ScalarUnaryOperator variogram = InversePowerVariogram.of(1);
    VectorLogManifold vectorLogManifold = Se2CoveringManifold.INSTANCE;
    for (int length = 5; length < 10; ++length) {
      Tensor sequence = RandomVariate.of(distribution, length, 3);
      Tensor point = RandomVariate.of(distribution, 3);
      PairwiseDistances d1 = PairwiseDistances.frobenius(vectorLogManifold, variogram, sequence);
      PairwiseDistances d2 = PairwiseDistances.norm2(vectorLogManifold, variogram, sequence);
      BiinvariantVector v1 = d1.biinvariantVector(point);
      BiinvariantVector v2 = d2.biinvariantVector(point);
      assertEquals(v1.coordinate().length(), v2.coordinate().length());
      // Chop._10.requireClose(v1.coordinate(), v2.coordinate());
    }
  }
}

// code by jph
package ch.ethz.idsc.sophus.krg;

import ch.ethz.idsc.sophus.hs.VectorLogManifold;
import ch.ethz.idsc.sophus.hs.sn.SnManifold;
import ch.ethz.idsc.sophus.hs.sn.SnRandomSample;
import ch.ethz.idsc.sophus.lie.rn.RnManifold;
import ch.ethz.idsc.sophus.lie.se2.Se2Manifold;
import ch.ethz.idsc.sophus.math.sample.RandomSample;
import ch.ethz.idsc.sophus.math.sample.RandomSampleInterface;
import ch.ethz.idsc.sophus.math.var.InversePowerVariogram;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.pdf.UniformDistribution;
import ch.ethz.idsc.tensor.sca.Chop;
import ch.ethz.idsc.tensor.sca.ScalarUnaryOperator;
import junit.framework.TestCase;

/** anchor == target */
public class TargetDistancesTest extends TestCase {
  public void testRn() {
    ScalarUnaryOperator variogram = InversePowerVariogram.of(1.3);
    Tensor sequence = RandomVariate.of(UniformDistribution.unit(), 10, 3);
    VectorLogManifold vectorLogManifold = RnManifold.INSTANCE;
    TensorUnaryOperator w1 = Biinvariant.ANCHOR.distances(vectorLogManifold, variogram, sequence);
    TensorUnaryOperator w2 = Biinvariant.TARGET.distances(vectorLogManifold, variogram, sequence);
    for (int count = 0; count < 10; ++count) {
      Tensor point = RandomVariate.of(UniformDistribution.unit(), 3);
      Chop._08.requireClose(w1.apply(point), w2.apply(point));
    }
  }

  public void testSn() {
    RandomSampleInterface randomSampleInterface = SnRandomSample.of(2);
    ScalarUnaryOperator variogram = InversePowerVariogram.of(1.4);
    Tensor sequence = RandomSample.of(randomSampleInterface, 10);
    VectorLogManifold vectorLogManifold = SnManifold.INSTANCE;
    TensorUnaryOperator w1 = Biinvariant.ANCHOR.distances(vectorLogManifold, variogram, sequence);
    TensorUnaryOperator w2 = Biinvariant.TARGET.distances(vectorLogManifold, variogram, sequence);
    for (int count = 0; count < 10; ++count) {
      Tensor point = RandomSample.of(randomSampleInterface);
      Chop._08.requireClose(w1.apply(point), w2.apply(point));
    }
  }

  public void testSe2() {
    ScalarUnaryOperator variogram = InversePowerVariogram.of(1.5);
    Distribution distribution = UniformDistribution.unit();
    Tensor sequence = RandomVariate.of(distribution, 10, 3);
    VectorLogManifold vectorLogManifold = Se2Manifold.INSTANCE;
    TensorUnaryOperator w1 = Biinvariant.ANCHOR.distances(vectorLogManifold, variogram, sequence);
    TensorUnaryOperator w2 = Biinvariant.TARGET.distances(vectorLogManifold, variogram, sequence);
    for (int count = 0; count < 10; ++count) {
      Tensor point = RandomVariate.of(distribution, 3);
      Chop._08.requireClose(w1.apply(point), w2.apply(point));
    }
  }

  public void testNullFail() {
    try {
      LeverageDistances.of(Se2Manifold.INSTANCE, null);
      fail();
    } catch (Exception exception) {
      // ---
    }
  }
}

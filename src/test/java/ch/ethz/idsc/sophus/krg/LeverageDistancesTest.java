// code by jph
package ch.ethz.idsc.sophus.krg;

import ch.ethz.idsc.sophus.hs.VectorLogManifold;
import ch.ethz.idsc.sophus.hs.sn.SnManifold;
import ch.ethz.idsc.sophus.hs.sn.SnRandomSample;
import ch.ethz.idsc.sophus.lie.rn.RnManifold;
import ch.ethz.idsc.sophus.lie.se2.Se2Manifold;
import ch.ethz.idsc.sophus.math.sample.RandomSample;
import ch.ethz.idsc.sophus.math.sample.RandomSampleInterface;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.pdf.UniformDistribution;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

/** anchor == target */
public class LeverageDistancesTest extends TestCase {
  public void testRn() {
    Tensor sequence = RandomVariate.of(UniformDistribution.unit(), 10, 3);
    VectorLogManifold vectorLogManifold = RnManifold.INSTANCE;
    TensorUnaryOperator w1 = Biinvariants.ANCHOR.distances(vectorLogManifold, sequence);
    TensorUnaryOperator w2 = Biinvariants.TARGET.distances(vectorLogManifold, sequence);
    for (int count = 0; count < 10; ++count) {
      Tensor point = RandomVariate.of(UniformDistribution.unit(), 3);
      Chop._08.requireClose(w1.apply(point), w2.apply(point));
    }
  }

  public void testSn() {
    RandomSampleInterface randomSampleInterface = SnRandomSample.of(2);
    Tensor sequence = RandomSample.of(randomSampleInterface, 10);
    VectorLogManifold vectorLogManifold = SnManifold.INSTANCE;
    TensorUnaryOperator w1 = Biinvariants.ANCHOR.distances(vectorLogManifold, sequence);
    TensorUnaryOperator w2 = Biinvariants.TARGET.distances(vectorLogManifold, sequence);
    for (int count = 0; count < 10; ++count) {
      Tensor point = RandomSample.of(randomSampleInterface);
      Chop._08.requireClose(w1.apply(point), w2.apply(point));
    }
  }

  public void testSe2() {
    Distribution distribution = UniformDistribution.unit();
    Tensor sequence = RandomVariate.of(distribution, 10, 3);
    VectorLogManifold vectorLogManifold = Se2Manifold.INSTANCE;
    TensorUnaryOperator w1 = Biinvariants.ANCHOR.distances(vectorLogManifold, sequence);
    TensorUnaryOperator w2 = Biinvariants.TARGET.distances(vectorLogManifold, sequence);
    for (int count = 0; count < 10; ++count) {
      Tensor point = RandomVariate.of(distribution, 3);
      Chop._08.requireClose(w1.apply(point), w2.apply(point));
    }
  }

  public void testNullFail() {
    try {
      LeverageDistances.of(null);
      fail();
    } catch (Exception exception) {
      // ---
    }
  }
}

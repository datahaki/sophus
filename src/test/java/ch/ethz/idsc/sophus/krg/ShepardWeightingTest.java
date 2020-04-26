// code by jph
package ch.ethz.idsc.sophus.krg;

import java.io.IOException;

import ch.ethz.idsc.sophus.hs.sn.SnPhongMean;
import ch.ethz.idsc.sophus.hs.sn.SnRandomSample;
import ch.ethz.idsc.sophus.itp.CrossAveraging;
import ch.ethz.idsc.sophus.lie.se2c.Se2CoveringManifold;
import ch.ethz.idsc.sophus.math.WeightingInterface;
import ch.ethz.idsc.sophus.math.sample.RandomSample;
import ch.ethz.idsc.sophus.math.sample.RandomSampleInterface;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.VectorQ;
import ch.ethz.idsc.tensor.io.Serialization;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.NormalDistribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import junit.framework.TestCase;

public class ShepardWeightingTest extends TestCase {
  public void testSimple() throws ClassNotFoundException, IOException {
    for (PseudoDistances pseudoDistances : PseudoDistances.values()) {
      WeightingInterface shepardInterpolation = Serialization.copy(ShepardWeighting.of( //
          pseudoDistances.create(Se2CoveringManifold.INSTANCE, InversePowerVariogram.of(2))));
      Distribution distribution = NormalDistribution.standard();
      for (int n = 10; n < 20; ++n) {
        Tensor sequence = RandomVariate.of(distribution, n, 3);
        RandomSampleInterface randomSampleInterface = SnRandomSample.of(4);
        Tensor values = RandomSample.of(randomSampleInterface, n);
        Tensor point = RandomVariate.of(distribution, 3);
        Tensor evaluate = CrossAveraging.of(shepardInterpolation, sequence, SnPhongMean.INSTANCE, values).apply(point);
        VectorQ.requireLength(evaluate, 5);
      }
    }
  }
}

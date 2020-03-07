// code by jph
package ch.ethz.idsc.sophus.lie.so3;

import ch.ethz.idsc.sophus.lie.LieGroupElement;
import ch.ethz.idsc.sophus.lie.LieGroupOps;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.NormalDistribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class So3AffineCoordinateTest extends TestCase {
  public void testLinearReproduction() {
    int fail = 0;
    Distribution distribution = NormalDistribution.of(0.0, 0.3);
    Distribution d2 = NormalDistribution.of(0.0, 0.1);
    for (int n = 4; n < 10; ++n)
      try {
        Tensor sequence = Tensor.of(RandomVariate.of(distribution, n, 3).stream().map(So3Exponential.INSTANCE::exp));
        Tensor mean = So3Exponential.INSTANCE.exp(RandomVariate.of(d2, 3));
        Tensor weights1 = So3AffineCoordinate.INSTANCE.weights(sequence, mean);
        Tensor o2 = So3BiinvariantMean.INSTANCE.mean(sequence, weights1);
        Chop._08.requireClose(mean, o2);
        // ---
        LieGroupElement lieGroupElement = So3Group.INSTANCE.element(TestHelper.spawn_So3());
        Tensor seqlft = Tensor.of(sequence.stream().map(lieGroupElement::combine));
        Tensor weights2 = So3AffineCoordinate.INSTANCE.weights(seqlft, lieGroupElement.combine(mean));
        Chop._10.requireClose(weights1, weights2);
        // ---
        Tensor seqinv = new LieGroupOps(So3Group.INSTANCE).allInvert(sequence);
        Tensor weights3 = So3AffineCoordinate.INSTANCE.weights( //
            seqinv, So3Group.INSTANCE.element(mean).inverse().toCoordinate());
        Chop._10.requireClose(weights1, weights3);
      } catch (Exception exception) {
        ++fail;
      }
    assertTrue(fail < 3);
  }
}

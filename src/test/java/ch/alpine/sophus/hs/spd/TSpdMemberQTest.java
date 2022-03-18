// code by jph
package ch.alpine.sophus.hs.spd;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.math.sample.RandomSample;
import ch.alpine.sophus.math.sample.RandomSampleInterface;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.sca.Clips;

public class TSpdMemberQTest {
  @Test
  public void testSimple() {
    for (int n = 1; n < 10; ++n) {
      RandomSampleInterface rsi = new TSpdRandomSample(n, UniformDistribution.of(Clips.absolute(1)));
      Tensor sim = RandomSample.of(rsi);
      TSpdMemberQ.INSTANCE.require(sim);
      Tolerance.CHOP.requireClose(TSpdMemberQ.project(sim), sim);
    }
  }
}

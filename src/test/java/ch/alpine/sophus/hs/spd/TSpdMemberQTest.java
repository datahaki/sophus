// code by jph
package ch.alpine.sophus.hs.spd;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.mat.IdentityMatrix;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.pdf.RandomSample;
import ch.alpine.tensor.pdf.RandomSampleInterface;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.sca.Clips;

class TSpdMemberQTest {
  @Test
  void testSimple() {
    for (int n = 1; n < 10; ++n) {
      RandomSampleInterface rsi = new TSpdRandomSample(n, UniformDistribution.of(Clips.absolute(1)));
      Tensor sim = RandomSample.of(rsi);
      SpdExponential spdExponential = new SpdExponential(IdentityMatrix.of(n));
      spdExponential.isTangentQ().require(sim);
      Tolerance.CHOP.requireClose(TSpdMemberQ.project(sim), sim);
    }
  }
}

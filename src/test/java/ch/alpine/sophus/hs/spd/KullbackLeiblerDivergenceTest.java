// code by jph
package ch.alpine.sophus.hs.spd;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.mat.re.Det;
import ch.alpine.tensor.mat.re.LinearSolve;
import ch.alpine.tensor.pdf.RandomSample;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.sca.Chop;
import ch.alpine.tensor.sca.Clips;

class KullbackLeiblerDivergenceTest {
  @SuppressWarnings("unused")
  @Test
  void testSimple() {
    Spd0RandomSample spdRandomSample = new Spd0RandomSample(3, UniformDistribution.of(Clips.absolute(1)));
    Tensor p = RandomSample.of(spdRandomSample);
    Tensor q = RandomSample.of(spdRandomSample);
    Chop._06.requireClose( //
        Det.of(p).divide(Det.of(q)), //
        Det.of(LinearSolve.of(q, p)));
    Scalar r1 = KullbackLeiblerDivergence.between(p, q);
    Scalar r2 = KullbackLeiblerDivergence.INSTANCE.distance(p, q);
    // System.out.println(r1);
    // System.out.println(r2);
  }
}

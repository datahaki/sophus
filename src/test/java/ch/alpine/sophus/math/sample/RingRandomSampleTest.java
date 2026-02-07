// code by jph
package ch.alpine.sophus.math.sample;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.RepeatedTest;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.VectorQ;
import ch.alpine.tensor.pdf.RandomSample;
import ch.alpine.tensor.sca.Clip;
import ch.alpine.tensor.sca.Clips;

class RingRandomSampleTest {
  @RepeatedTest(5)
  void test() {
    double lo = Math.random();
    RingRandomSample ringRandomSample = new RingRandomSample(1, RealScalar.of(lo), RealScalar.ONE);
    Tensor vector = RandomSample.of(ringRandomSample);
    assertTrue(VectorQ.ofLength(vector, 1));
    Scalar x = vector.Get(0);
    Clip c1 = Clips.interval(-1, -lo);
    Clip c2 = Clips.interval(+lo, +1);
    assertTrue(c1.isInside(x) || c2.isInside(x));
  }
}

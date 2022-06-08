// code by jph
package ch.alpine.sophus.hs.s2;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.api.Exponential;
import ch.alpine.sophus.hs.sn.SnMetric;
import ch.alpine.sophus.hs.sn.SnRandomSample;
import ch.alpine.sophus.math.sample.RandomSample;
import ch.alpine.sophus.math.sample.RandomSampleInterface;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.VectorQ;
import ch.alpine.tensor.nrm.Vector2Norm;
import ch.alpine.tensor.sca.Chop;

class S2ManifoldTest {
  @Test
  public void testTangentSpace() {
    RandomSampleInterface randomSampleInterface = SnRandomSample.of(2);
    for (int count = 0; count < 10; ++count) {
      Tensor p = RandomSample.of(randomSampleInterface);
      Tensor q = RandomSample.of(randomSampleInterface);
      Exponential exponential = S2Manifold.INSTANCE.exponential(p);
      Tensor log = exponential.vectorLog(q);
      VectorQ.requireLength(log, 2);
      Chop._08.requireClose(Vector2Norm.of(log), SnMetric.INSTANCE.distance(p, q));
    }
  }

  @Test
  public void testExp() {
    RandomSampleInterface randomSampleInterface = SnRandomSample.of(2);
    for (int count = 0; count < 10; ++count) {
      Tensor p = RandomSample.of(randomSampleInterface);
      Tensor q = RandomSample.of(randomSampleInterface);
      Exponential exponential = S2Manifold.INSTANCE.exponential(p);
      Tensor log = exponential.log(q);
      VectorQ.requireLength(log, 2);
      Tensor r = exponential.exp(log);
      Chop._08.requireClose(q, r);
    }
  }
}

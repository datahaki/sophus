// code by jph
package ch.ethz.idsc.sophus.hs.s2;

import ch.ethz.idsc.sophus.hs.TangentSpace;
import ch.ethz.idsc.sophus.hs.sn.SnMetric;
import ch.ethz.idsc.sophus.hs.sn.SnRandomSample;
import ch.ethz.idsc.sophus.math.Exponential;
import ch.ethz.idsc.sophus.math.sample.RandomSample;
import ch.ethz.idsc.sophus.math.sample.RandomSampleInterface;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.VectorQ;
import ch.ethz.idsc.tensor.red.Hypot;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class S2ManifoldTest extends TestCase {
  public void testTangentSpace() {
    RandomSampleInterface randomSampleInterface = SnRandomSample.of(2);
    for (int count = 0; count < 10; ++count) {
      Tensor p = RandomSample.of(randomSampleInterface);
      Tensor q = RandomSample.of(randomSampleInterface);
      TangentSpace tangentSpace = S2Manifold.INSTANCE.logAt(p);
      Tensor log = tangentSpace.vectorLog(q);
      VectorQ.requireLength(log, 2);
      Chop._08.requireClose(Hypot.ofVector(log), SnMetric.INSTANCE.distance(p, q));
    }
  }

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
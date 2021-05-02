// code by jph
package ch.alpine.sophus.lie.so;

import ch.alpine.sophus.lie.so3.So3RandomSample;
import ch.alpine.sophus.math.sample.RandomSample;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Transpose;
import ch.alpine.tensor.mat.Inverse;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.NormalDistribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.sca.Chop;
import junit.framework.TestCase;

public class SoTransportTest extends TestCase {
  public void testSimple() {
    Distribution distribution = NormalDistribution.standard();
    Tensor m = RandomVariate.of(distribution, 3, 3);
    Tensor ve = Transpose.of(m).subtract(m);
    Tensor p = RandomSample.of(So3RandomSample.INSTANCE);
    Tensor q = RandomSample.of(So3RandomSample.INSTANCE);
    Tensor vp = p.dot(ve);
    Tensor vq = SoTransport.INSTANCE.shift(p, q).apply(vp);
    Chop._10.requireClose(q.dot(ve), vq);
    Chop._10.requireClose(q.dot(Inverse.of(p)).dot(vp), vq);
  }
}

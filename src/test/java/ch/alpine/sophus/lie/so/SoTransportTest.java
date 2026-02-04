// code by jph
package ch.alpine.sophus.lie.so;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Transpose;
import ch.alpine.tensor.mat.re.Inverse;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomSample;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.sca.Chop;

class SoTransportTest {
  @Test
  void testSimple() {
    Distribution distribution = NormalDistribution.standard();
    Tensor m = RandomVariate.of(distribution, 3, 3);
    Tensor ve = Transpose.of(m).subtract(m);
    Tensor p = RandomSample.of(So3Group.INSTANCE);
    Tensor q = RandomSample.of(So3Group.INSTANCE);
    Tensor vp = p.dot(ve);
    Tensor vq = SoTransport.INSTANCE.shift(p, q).apply(vp);
    Chop._10.requireClose(q.dot(ve), vq);
    Chop._10.requireClose(q.dot(Inverse.of(p)).dot(vp), vq);
  }
}

// code by jph
package ch.ethz.idsc.sophus.lie.son;

import ch.ethz.idsc.sophus.lie.so3.So3RandomSample;
import ch.ethz.idsc.sophus.math.sample.RandomSample;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.Transpose;
import ch.ethz.idsc.tensor.mat.Inverse;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.NormalDistribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class SonTransportTest extends TestCase {
  public void testSimple() {
    Distribution distribution = NormalDistribution.standard();
    Tensor m = RandomVariate.of(distribution, 3, 3);
    Tensor ve = Transpose.of(m).subtract(m);
    Tensor p = RandomSample.of(So3RandomSample.INSTANCE);
    Tensor q = RandomSample.of(So3RandomSample.INSTANCE);
    Tensor vp = p.dot(ve);
    Tensor vq = SonTransport.INSTANCE.shift(p, q).apply(vp);
    Chop._10.requireClose(q.dot(ve), vq);
    Chop._10.requireClose(q.dot(Inverse.of(p)).dot(vp), vq);
  }
}

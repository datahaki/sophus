// code by jph
package ch.ethz.idsc.sophus.lie.so3;

import ch.ethz.idsc.sophus.math.sample.RandomSample;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.Transpose;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.NormalDistribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import junit.framework.TestCase;

public class So3TransportTest extends TestCase {
  public void testSimple() {
    Distribution distribution = NormalDistribution.standard();
    Tensor m = RandomVariate.of(distribution, 3, 3);
    Tensor ve = Transpose.of(m).subtract(m);
    Tensor p = RandomSample.of(So3RandomSample.INSTANCE);
    Tensor q = RandomSample.of(So3RandomSample.INSTANCE);
    Tensor vp = p.dot(ve);
    StaticHelper.requireTangent(p, vp);
    Tensor vq = So3Transport.INSTANCE.shift(p, q).apply(vp);
    StaticHelper.requireTangent(q, vq);
  }
}

// code by jph
package ch.ethz.idsc.sophus.lie.so3;

import ch.ethz.idsc.sophus.hs.HsTransport;
import ch.ethz.idsc.sophus.hs.PoleLadder;
import ch.ethz.idsc.sophus.hs.SubdivideTransport;
import ch.ethz.idsc.sophus.math.sample.RandomSample;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.Transpose;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.NormalDistribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.red.Frobenius;
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

  public void testPoleLadder() {
    Distribution distribution = NormalDistribution.standard();
    Tensor m = RandomVariate.of(distribution, 3, 3);
    Tensor ve = Transpose.of(m).subtract(m); // skew symmetric
    Tensor p = RandomSample.of(So3RandomSample.INSTANCE);
    Tensor q = RandomSample.of(So3RandomSample.INSTANCE);
    Tensor vp = p.dot(ve); // transport from e to p
    StaticHelper.requireTangent(p, vp);
    Tensor vq1 = So3Transport.INSTANCE.shift(p, q).apply(vp);
    StaticHelper.requireTangent(q, vq1);
    HsTransport hsTransport = PoleLadder.of(So3Manifold.INSTANCE);
    Tensor vq2 = hsTransport.shift(p, q).apply(vp);
    Tensor vq3 = SubdivideTransport.of(hsTransport, So3Manifold.INSTANCE, 100).shift(p, q).apply(vp);
    // TODO does not match
    // System.out.println(vq1);
    // System.out.println(vq2);
    Scalar error2 = Frobenius.between(vq1, vq2);
    Scalar error3 = Frobenius.between(vq1, vq3);
    error2.subtract(error3);
    // System.out.println(error2);
    // System.out.println(error3);
    // assertTrue(Scalars.lessThan(error2, RealScalar.of(6)));
    // assertTrue(Scalars.lessThan(error3, error2));
  }
}

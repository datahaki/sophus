// code by jph
package ch.ethz.idsc.sophus.lie.son;

import ch.ethz.idsc.sophus.hs.HsTransport;
import ch.ethz.idsc.sophus.hs.PoleLadder;
import ch.ethz.idsc.sophus.hs.SubdivideTransport;
import ch.ethz.idsc.sophus.lie.so3.So3Geodesic;
import ch.ethz.idsc.sophus.lie.so3.So3Manifold;
import ch.ethz.idsc.sophus.lie.so3.So3RandomSample;
import ch.ethz.idsc.sophus.math.sample.RandomSample;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.Transpose;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.NormalDistribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.red.Frobenius;
import junit.framework.TestCase;

public class SonTransportTest extends TestCase {
  public void testSimple() {
    Distribution distribution = NormalDistribution.standard();
    Tensor m = RandomVariate.of(distribution, 3, 3);
    Tensor ve = Transpose.of(m).subtract(m);
    Tensor p = RandomSample.of(So3RandomSample.INSTANCE);
    Tensor q = RandomSample.of(So3RandomSample.INSTANCE);
    Tensor vp = p.dot(ve);
    new TSonMemberQ(p).require(vp);
    Tensor vq = SonTransport.INSTANCE.shift(p, q).apply(vp);
    new TSonMemberQ(q).require(vq);
  }

  public void testPoleLadder() {
    Distribution distribution = NormalDistribution.standard();
    Tensor m = RandomVariate.of(distribution, 3, 3);
    Tensor ve = Transpose.of(m).subtract(m); // skew symmetric
    Tensor p = RandomSample.of(So3RandomSample.INSTANCE);
    Tensor q = RandomSample.of(So3RandomSample.INSTANCE);
    Tensor vp = p.dot(ve); // transport from e to p
    new TSonMemberQ(p).require(vp);
    Tensor vq1 = SonTransport.INSTANCE.shift(p, q).apply(vp);
    new TSonMemberQ(q).require(vq1);
    HsTransport hsTransport = PoleLadder.of(So3Manifold.HS_EXP);
    Tensor vq2 = hsTransport.shift(p, q).apply(vp);
    Tensor vq3 = SubdivideTransport.of(hsTransport, So3Geodesic.INSTANCE, 100).shift(p, q).apply(vp);
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

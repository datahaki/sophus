// code by jph
package ch.alpine.sophus.hs.hn;

import ch.alpine.sophus.hs.HsTransport;
import ch.alpine.sophus.hs.PoleLadder;
import ch.alpine.sophus.usr.AssertFail;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.TensorUnaryOperator;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.TrapezoidalDistribution;
import ch.alpine.tensor.sca.Chop;
import junit.framework.TestCase;

public class HnTransportTest extends TestCase {
  private static final HsTransport POLE_LADDER = PoleLadder.of(HnManifold.INSTANCE);

  public void testSimple() {
    int d = 3;
    Distribution distribution = TrapezoidalDistribution.of(-3, -1, 1, 3);
    Tensor x = RandomVariate.of(distribution, d);
    Tensor p = HnWeierstrassCoordinate.toPoint(x);
    Tensor q = HnWeierstrassCoordinate.toPoint(RandomVariate.of(distribution, d));
    TensorUnaryOperator s1 = HnTransport.INSTANCE.shift(p, q);
    TensorUnaryOperator s2 = POLE_LADDER.shift(p, q);
    Tensor vpq = new HnExponential(p).log(q);
    Tensor vqp = s1.apply(vpq).negate();
    Tensor exp = new HnExponential(q).exp(vqp);
    Chop._08.requireClose(p, exp);
    Tensor pv = HnWeierstrassCoordinate.toTangent(x, RandomVariate.of(distribution, d));
    Chop._08.requireClose(s1.apply(pv), s2.apply(pv));
  }

  public void testNonMemberFail() {
    int d = 3;
    Distribution distribution = TrapezoidalDistribution.of(-3, -1, 1, 3);
    Tensor p = HnWeierstrassCoordinate.toPoint(RandomVariate.of(distribution, d));
    Tensor q = HnWeierstrassCoordinate.toPoint(RandomVariate.of(distribution, d));
    TensorUnaryOperator shift = HnTransport.INSTANCE.shift(p, q);
    Tensor v = RandomVariate.of(distribution, d + 1);
    AssertFail.of(() -> shift.apply(v));
  }
}

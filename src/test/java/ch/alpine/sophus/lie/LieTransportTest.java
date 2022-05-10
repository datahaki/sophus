// code by jph
package ch.alpine.sophus.lie;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.hs.HsTransport;
import ch.alpine.sophus.hs.PoleLadder;
import ch.alpine.sophus.hs.SubdivideTransport;
import ch.alpine.sophus.hs.SymmetrizeTransport;
import ch.alpine.sophus.lie.se2c.Se2CoveringGroup;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.sca.Chop;

class LieTransportTest {
  private static final Distribution DISTRIBUTION = UniformDistribution.of(-10, 10);

  @Test
  public void testSimple() {
    Tensor p = RandomVariate.of(DISTRIBUTION, 3);
    Tensor q = RandomVariate.of(DISTRIBUTION, 3);
    Tensor v = RandomVariate.of(DISTRIBUTION, 3);
    Tensor r1 = LieTransport.INSTANCE.shift(p, q).apply(v);
    HsTransport hsTransport = new PoleLadder(Se2CoveringGroup.INSTANCE);
    Tensor r2 = hsTransport.shift(p, q).apply(v);
    Chop._10.requireClose(v.Get(2), r1.Get(2));
    Chop._10.requireClose(v.Get(2), r2.Get(2));
    HsTransport transport = new SymmetrizeTransport(hsTransport);
    Tensor r3 = transport.shift(p, q).apply(v);
    Chop._10.requireClose(r2, r3);
  }

  @Test
  public void testSubdivide() {
    Tensor p = RandomVariate.of(DISTRIBUTION, 3);
    Tensor q = RandomVariate.of(DISTRIBUTION, 3);
    Tensor v = RandomVariate.of(DISTRIBUTION, 3);
    Tensor r1 = LieTransport.INSTANCE.shift(p, q).apply(v);
    HsTransport hsTransport = SubdivideTransport.of( //
        new PoleLadder(Se2CoveringGroup.INSTANCE), //
        Se2CoveringGroup.INSTANCE, 100);
    Tensor r2 = hsTransport.shift(p, q).apply(v);
    Chop._08.requireClose(v.Get(2), r1.Get(2));
    Chop._08.requireClose(v.Get(2), r2.Get(2));
  }
}

// code by jph
package ch.ethz.idsc.sophus.lie.se2c;

import ch.ethz.idsc.sophus.hs.HsTransport;
import ch.ethz.idsc.sophus.hs.PoleLadder;
import ch.ethz.idsc.sophus.hs.SubdivideTransport;
import ch.ethz.idsc.sophus.hs.SymmetrizeTransport;
import ch.ethz.idsc.sophus.lie.LieTransport;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class LieTransportTest extends TestCase {
  public void testSimple() {
    Tensor p = TestHelper.spawn_Se2C();
    Tensor q = TestHelper.spawn_Se2C();
    Tensor v = TestHelper.spawn_se2C();
    Tensor r1 = LieTransport.INSTANCE.shift(p, q).apply(v);
    HsTransport hsTransport = PoleLadder.of(Se2CoveringManifold.INSTANCE);
    Tensor r2 = hsTransport.shift(p, q).apply(v);
    Chop._10.requireClose(v.Get(2), r1.Get(2));
    Chop._10.requireClose(v.Get(2), r2.Get(2));
    HsTransport transport = SymmetrizeTransport.of(hsTransport);
    Tensor r3 = transport.shift(p, q).apply(v);
    Chop._10.requireClose(r2, r3);
  }

  public void testSubdivide() {
    Tensor p = TestHelper.spawn_Se2C();
    Tensor q = TestHelper.spawn_Se2C();
    Tensor v = TestHelper.spawn_se2C();
    Tensor r1 = LieTransport.INSTANCE.shift(p, q).apply(v);
    HsTransport hsTransport = SubdivideTransport.of( //
        PoleLadder.of(Se2CoveringManifold.INSTANCE), //
        Se2CoveringGeodesic.INSTANCE, 100);
    Tensor r2 = hsTransport.shift(p, q).apply(v);
    Chop._08.requireClose(v.Get(2), r1.Get(2));
    Chop._08.requireClose(v.Get(2), r2.Get(2));
  }
}

// code by jph
package ch.ethz.idsc.sophus.hs.hn;

import java.io.IOException;

import ch.ethz.idsc.sophus.usr.AssertFail;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.ext.Serialization;
import ch.ethz.idsc.tensor.mat.Tolerance;
import ch.ethz.idsc.tensor.pdf.NormalDistribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import junit.framework.TestCase;

public class THnMemberQTest extends TestCase {
  public void testProject() throws ClassNotFoundException, IOException {
    Tensor x = HnWeierstrassCoordinate.toPoint(RandomVariate.of(NormalDistribution.standard(), 3));
    HnMemberQ.INSTANCE.require(x);
    Tensor v = RandomVariate.of(NormalDistribution.standard(), 4);
    THnMemberQ tHnMemberQ = Serialization.copy(new THnMemberQ(x));
    assertFalse(tHnMemberQ.test(v));
    Tensor xv = tHnMemberQ.project(v);
    assertTrue(tHnMemberQ.test(xv));
    Tensor xw = tHnMemberQ.project(xv);
    Tolerance.CHOP.requireClose(xv, xw);
  }

  public void testNullFail() {
    AssertFail.of(() -> new THnMemberQ(null));
  }
}

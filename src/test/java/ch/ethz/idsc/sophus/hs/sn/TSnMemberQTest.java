// code by jph
package ch.ethz.idsc.sophus.hs.sn;

import java.io.IOException;

import ch.ethz.idsc.sophus.math.MemberQ;
import ch.ethz.idsc.sophus.math.sample.RandomSample;
import ch.ethz.idsc.sophus.math.sample.RandomSampleInterface;
import ch.ethz.idsc.sophus.usr.AssertFail;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.UnitVector;
import ch.ethz.idsc.tensor.ext.Serialization;
import ch.ethz.idsc.tensor.pdf.NormalDistribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import junit.framework.TestCase;

public class TSnMemberQTest extends TestCase {
  public void testSerializable() throws ClassNotFoundException, IOException {
    MemberQ memberQ = Serialization.copy(SnMemberQ.INSTANCE);
    memberQ.require(UnitVector.of(4, 3));
    TSnMemberQ tSnMemberQ = Serialization.copy(new TSnMemberQ(UnitVector.of(4, 3)));
    tSnMemberQ.require(UnitVector.of(4, 2));
    assertFalse(tSnMemberQ.test(Tensors.vector(1, 2, 3, 4)));
  }

  public void testSnExpLog() {
    for (int d = 1; d < 4; ++d) {
      RandomSampleInterface randomSampleInterface = SnRandomSample.of(d);
      Tensor x = RandomSample.of(randomSampleInterface);
      Tensor y = RandomSample.of(randomSampleInterface);
      Tensor v = new SnExponential(x).log(y);
      TSnMemberQ tSnMemberQ = new TSnMemberQ(x);
      tSnMemberQ.require(v);
      Tensor w = RandomVariate.of(NormalDistribution.standard(), d + 1);
      assertFalse(tSnMemberQ.test(w));
      w = tSnMemberQ.project(w);
      assertTrue(tSnMemberQ.test(w));
    }
  }

  public void testNullFail() {
    AssertFail.of(() -> new TSnMemberQ(null));
  }
}

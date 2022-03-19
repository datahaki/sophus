// code by jph
package ch.alpine.sophus.hs.sn;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.api.MemberQ;
import ch.alpine.sophus.math.sample.RandomSample;
import ch.alpine.sophus.math.sample.RandomSampleInterface;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.UnitVector;
import ch.alpine.tensor.ext.Serialization;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;

public class TSnMemberQTest {
  @Test
  public void testSerializable() throws ClassNotFoundException, IOException {
    MemberQ memberQ = Serialization.copy(SnMemberQ.INSTANCE);
    memberQ.require(UnitVector.of(4, 3));
    TSnMemberQ tSnMemberQ = Serialization.copy(new TSnMemberQ(UnitVector.of(4, 3)));
    tSnMemberQ.require(UnitVector.of(4, 2));
    assertFalse(tSnMemberQ.test(Tensors.vector(1, 2, 3, 4)));
  }

  @Test
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
      Tensor w2 = tSnMemberQ.project(w);
      Tolerance.CHOP.requireClose(w, w2);
    }
  }

  @Test
  public void testNullFail() {
    assertThrows(Exception.class, () -> new TSnMemberQ(null));
  }
}

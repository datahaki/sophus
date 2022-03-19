// code by jph
package ch.alpine.sophus.hs.hn;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.ext.Serialization;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;

public class THnMemberQTest {
  @Test
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
    Tensor nv = HnVectorNorm.NORMALIZE.apply(xv);
    Tensor nw = tHnMemberQ.project(nv);
    Tolerance.CHOP.requireClose(nv, nw);
  }

  @Test
  public void testNullFail() {
    assertThrows(Exception.class, () -> new THnMemberQ(null));
  }
}

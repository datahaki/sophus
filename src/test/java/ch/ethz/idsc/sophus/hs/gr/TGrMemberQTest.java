// code by jph
package ch.ethz.idsc.sophus.hs.gr;

import java.io.IOException;
import java.util.Arrays;

import ch.ethz.idsc.sophus.math.sample.RandomSample;
import ch.ethz.idsc.sophus.usr.AssertFail;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.Dimensions;
import ch.ethz.idsc.tensor.ext.Serialization;
import ch.ethz.idsc.tensor.num.Pi;
import ch.ethz.idsc.tensor.pdf.NormalDistribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import junit.framework.TestCase;

public class TGrMemberQTest extends TestCase {
  public void testSerializable() throws ClassNotFoundException, IOException {
    int n = 8;
    Tensor x = RandomSample.of(GrRandomSample.of(n, 3));
    assertEquals(Dimensions.of(x), Arrays.asList(n, n));
    GrMemberQ.INSTANCE.require(x);
    Tensor pre = RandomVariate.of(NormalDistribution.standard(), n, n);
    TGrMemberQ tGrMemberQ = Serialization.copy(new TGrMemberQ(x));
    assertFalse(tGrMemberQ.test(pre));
    Tensor v = tGrMemberQ.project(pre).multiply(Pi.VALUE);
    tGrMemberQ.require(v);
  }

  public void testNullFail() {
    AssertFail.of(() -> new TGrMemberQ(null));
  }
}

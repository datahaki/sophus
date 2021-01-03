// code by jph
package ch.ethz.idsc.sophus.hs.gr;

import java.io.IOException;
import java.util.Arrays;

import ch.ethz.idsc.sophus.hs.HsMemberQ;
import ch.ethz.idsc.sophus.usr.AssertFail;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.Dimensions;
import ch.ethz.idsc.tensor.alg.Transpose;
import ch.ethz.idsc.tensor.ext.Serialization;
import ch.ethz.idsc.tensor.pdf.NormalDistribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class GrMemberQTest extends TestCase {
  public void testSerializable() throws ClassNotFoundException, IOException {
    HsMemberQ hsMemberQ = Serialization.copy(GrMemberQ.of(Chop._08));
    int n = 8;
    Tensor design = RandomVariate.of(NormalDistribution.standard(), n, 3);
    assertFalse(hsMemberQ.isPoint(design));
    assertFalse(hsMemberQ.isPoint(design.dot(Transpose.of(design))));
    Tensor x = StaticHelper.projection(design);
    assertEquals(Dimensions.of(x), Arrays.asList(n, n));
    hsMemberQ.requirePoint(x);
    Tensor pre = RandomVariate.of(NormalDistribution.standard(), n, n);
    assertFalse(hsMemberQ.isTangent(x, pre));
    Tensor v = StaticHelper.projectTangent(x, pre);
    hsMemberQ.requireTangent(x, v);
  }

  public void testNullFail() {
    AssertFail.of(() -> GrMemberQ.of(null));
  }
}

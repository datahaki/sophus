// code by jph
package ch.ethz.idsc.sophus.hs.s2;

import java.io.IOException;

import ch.ethz.idsc.sophus.hs.sn.SnMemberQ;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.Subdivide;
import ch.ethz.idsc.tensor.api.ScalarTensorFunction;
import ch.ethz.idsc.tensor.ext.Serialization;
import junit.framework.TestCase;

public class LoxodromeTest extends TestCase {
  public void testSimple() throws ClassNotFoundException, IOException {
    ScalarTensorFunction scalarTensorFunction = Serialization.copy(Loxodrome.of(RealScalar.of(0.1)));
    Tensor tensor = Subdivide.of(-1, 100, 60).map(scalarTensorFunction);
    assertTrue(tensor.stream().allMatch(SnMemberQ.INSTANCE::test));
  }
}

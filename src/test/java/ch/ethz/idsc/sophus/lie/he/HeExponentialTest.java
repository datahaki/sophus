// code by jph
package ch.ethz.idsc.sophus.lie.he;

import ch.ethz.idsc.sophus.lie.LieGroupOps;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.mat.Tolerance;
import junit.framework.TestCase;

public class HeExponentialTest extends TestCase {
  private static final LieGroupOps LIE_GROUP_OPS = new LieGroupOps(HeGroup.INSTANCE);

  public void testExpLog() {
    for (int count = 0; count < 10; ++count) {
      Tensor inp = TestHelper.spawn_He(2);
      Tensor xyz = HeExponential.INSTANCE.exp(inp);
      Tensor uvw = HeExponential.INSTANCE.log(xyz);
      Tolerance.CHOP.requireClose(inp, uvw);
    }
  }

  public void testLogExp() {
    for (int count = 0; count < 10; ++count) {
      Tensor inp = TestHelper.spawn_He(2);
      Tensor uvw = HeExponential.INSTANCE.log(inp);
      Tensor xyz = HeExponential.INSTANCE.exp(uvw);
      Tolerance.CHOP.requireClose(inp, xyz);
    }
  }

  public void testAdLog() {
    // lieGroupOps
    for (int count = 0; count < 10; ++count) {
      Tensor g = TestHelper.spawn_He(2);
      Tensor m = TestHelper.spawn_He(2);
      Tensor lhs = HeExponential.INSTANCE.log(LIE_GROUP_OPS.conjugate(g, m));
      Tensor rhs = HeGroup.INSTANCE.element(g).adjoint(HeExponential.INSTANCE.log(m));
      Tolerance.CHOP.requireClose(lhs, rhs);
    }
  }
}

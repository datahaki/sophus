// code by jph
package ch.ethz.idsc.sophus.hs.spd;

import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.lie.MatrixExp;
import ch.ethz.idsc.tensor.lie.MatrixLog;
import ch.ethz.idsc.tensor.mat.LowerTriangularize;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class SpdExponentialTest extends TestCase {
  public void testSimple() {
    for (int n = 1; n < 5; ++n) {
      Tensor x = TestHelper.generateSim(n);
      Tensor g = SpdExpLog.INSTANCE.exp(x);
      Tensor r = SpdExpLog.INSTANCE.log(g);
      Chop._07.requireClose(x, r);
    }
  }

  public void testMatrixExp() {
    for (int n = 1; n < 5; ++n) {
      Tensor x = TestHelper.generateSim(n);
      Tensor exp1 = SpdExpLog.INSTANCE.exp(x);
      Tensor exp2 = MatrixExp.of(x);
      Chop._07.requireClose(exp1, exp2);
    }
  }

  public void testMatrixLog() {
    for (int count = 0; count < 10; ++count) {
      Tensor x = TestHelper.generateSpd(2);
      Tensor exp1 = SpdExpLog.INSTANCE.log(x);
      Tensor exp2 = MatrixLog.of(x);
      Chop._08.requireClose(exp1, exp2);
    }
  }

  public void testExpNonSymmetricFail() {
    Tensor x = LowerTriangularize.of(TestHelper.generateSim(4));
    try {
      SpdExpLog.INSTANCE.exp(x);
      fail();
    } catch (Exception exception) {
      // ---
    }
  }

  public void testLogNonSymmetricFail() {
    Tensor g = LowerTriangularize.of(TestHelper.generateSpd(4));
    try {
      SpdExpLog.INSTANCE.log(g);
      fail();
    } catch (Exception exception) {
      // ---
    }
  }
}

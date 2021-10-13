// code by jph
package ch.alpine.sophus.hs.spd;

import ch.alpine.sophus.usr.AssertFail;
import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.mat.LowerTriangularize;
import ch.alpine.tensor.mat.ex.MatrixExp;
import ch.alpine.tensor.mat.ex.MatrixLog;
import ch.alpine.tensor.sca.Chop;
import junit.framework.TestCase;

public class Spd0ExponentialTest extends TestCase {
  public void testSimple() {
    for (int n = 1; n < 5; ++n) {
      Tensor x = TestHelper.generateSim(n);
      Tensor g = Spd0Exponential.INSTANCE.exp(x);
      Tensor r = Spd0Exponential.INSTANCE.log(g);
      Chop._07.requireClose(x, r);
      Tensor m1 = //
          Spd0Exponential.INSTANCE.exp(Spd0Exponential.INSTANCE.log(g).multiply(RationalScalar.HALF));
      Tensor m2 = //
          Spd0Exponential.INSTANCE.midpoint(g);
      Chop._07.requireClose(m1, m2);
    }
  }

  public void testMatrixExp() {
    for (int n = 1; n < 5; ++n) {
      Tensor x = TestHelper.generateSim(n);
      Tensor exp1 = Spd0Exponential.INSTANCE.exp(x);
      Tensor exp2 = MatrixExp.of(x);
      Chop._07.requireClose(exp1, exp2);
    }
  }

  public void testMatrixLog() {
    for (int count = 0; count < 10; ++count) {
      Tensor x = TestHelper.generateSpd(2);
      Tensor exp1 = Spd0Exponential.INSTANCE.log(x);
      Tensor exp2 = MatrixLog.of(x);
      Chop._08.requireClose(exp1, exp2);
    }
  }

  public void testExpNonSymmetricFail() {
    Tensor x = LowerTriangularize.of(TestHelper.generateSim(4));
    AssertFail.of(() -> Spd0Exponential.INSTANCE.exp(x));
  }

  public void testLogNonSymmetricFail() {
    Tensor g = LowerTriangularize.of(TestHelper.generateSpd(4));
    AssertFail.of(() -> Spd0Exponential.INSTANCE.log(g));
  }
}

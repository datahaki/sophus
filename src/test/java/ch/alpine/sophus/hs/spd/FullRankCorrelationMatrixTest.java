// code by jph
package ch.alpine.sophus.hs.spd;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.ConstantArray;
import ch.alpine.tensor.mat.IdentityMatrix;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.red.Diagonal;
import junit.framework.TestCase;

public class FullRankCorrelationMatrixTest extends TestCase {
  public void testSimple() {
    for (int n = 1; n < 6; ++n) {
      Tensor spd = TestHelper.generateSpd(n);
      Tensor frc = FullRankCorrelationMatrix.fromSpd(spd);
      Tensor diag = Diagonal.of(frc);
      Tolerance.CHOP.requireClose(diag, ConstantArray.of(RealScalar.ONE, n));
    }
  }

  public void testExp() {
    for (int n = 1; n < 6; ++n) {
      Tensor frc1 = FullRankCorrelationMatrix.fromSpd(TestHelper.generateSpd(n));
      Tensor frc2 = FullRankCorrelationMatrix.fromSpd(TestHelper.generateSpd(n));
      Tensor log = new SpdExponential(frc1).log(frc2);
      // System.out.println(Pretty.of(log.map(Round._2)));
    }
  }

  public void testExp0() {
    for (int n = 1; n < 6; ++n) {
      Tensor frc1 = IdentityMatrix.of(n);
      Tensor frc2 = FullRankCorrelationMatrix.fromSpd(TestHelper.generateSpd(n));
      Tensor log = new SpdExponential(frc1).log(frc2);
      // System.out.println(Pretty.of(log.map(Round._2)));
    }
  }
}

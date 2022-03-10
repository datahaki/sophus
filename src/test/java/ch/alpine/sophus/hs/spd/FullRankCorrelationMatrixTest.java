// code by jph
package ch.alpine.sophus.hs.spd;

import ch.alpine.sophus.math.sample.RandomSample;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.ConstantArray;
import ch.alpine.tensor.mat.IdentityMatrix;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.red.Diagonal;
import ch.alpine.tensor.sca.Clips;
import junit.framework.TestCase;

public class FullRankCorrelationMatrixTest extends TestCase {
  public void testSimple() {
    for (int n = 1; n < 6; ++n) {
      SpdRandomSample spdRandomSample = new SpdRandomSample(n, UniformDistribution.of(Clips.absolute(1)));
      Tensor spd = RandomSample.of(spdRandomSample);
      Tensor frc = FullRankCorrelationMatrix.fromSpd(spd);
      Tensor diag = Diagonal.of(frc);
      Tolerance.CHOP.requireClose(diag, ConstantArray.of(RealScalar.ONE, n));
    }
  }

  public void testExp() {
    for (int n = 1; n < 6; ++n) {
      SpdRandomSample spdRandomSample = new SpdRandomSample(n, UniformDistribution.of(Clips.absolute(1)));
      Tensor frc1 = FullRankCorrelationMatrix.fromSpd(RandomSample.of(spdRandomSample));
      Tensor frc2 = FullRankCorrelationMatrix.fromSpd(RandomSample.of(spdRandomSample));
      Tensor log = new SpdExponential(frc1).log(frc2);
      log.map(Scalar::zero);
      // System.out.println(Pretty.of(log.map(Round._2)));
    }
  }

  public void testExp0() {
    for (int n = 1; n < 6; ++n) {
      Tensor frc1 = IdentityMatrix.of(n);
      SpdRandomSample spdRandomSample = new SpdRandomSample(n, UniformDistribution.of(Clips.absolute(1)));
      Tensor frc2 = FullRankCorrelationMatrix.fromSpd(RandomSample.of(spdRandomSample));
      Tensor log = new SpdExponential(frc1).log(frc2);
      log.map(Scalar::zero);
      // System.out.println(Pretty.of(log.map(Round._2)));
    }
  }
}

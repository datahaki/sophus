// code by jph
package ch.alpine.sophus.hs.sn;

import ch.alpine.sophus.math.sample.RandomSample;
import ch.alpine.sophus.math.sample.RandomSampleInterface;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.lie.Cross;
import ch.alpine.tensor.lie.TensorWedge;
import ch.alpine.tensor.mat.OrthogonalMatrixQ;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.mat.re.Det;
import ch.alpine.tensor.mat.re.Inverse;
import ch.alpine.tensor.nrm.Vector2Norm;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.sca.Chop;
import junit.framework.TestCase;

public class SnRotationMatrixTest extends TestCase {
  private static final Distribution UNIFORM = NormalDistribution.standard();

  public void testMatch3D() {
    for (int count = 0; count < 20; ++count) {
      Tensor a = Vector2Norm.NORMALIZE.apply(RandomVariate.of(UNIFORM, 3));
      Tensor b = Vector2Norm.NORMALIZE.apply(RandomVariate.of(UNIFORM, 3));
      Tensor w = Cross.of(a, b);
      Tensor wx = Cross.skew3(w);
      Tensor wd = TensorWedge.of(a, b).negate();
      Tolerance.CHOP.requireClose(wx, wd);
      Tensor rotation1 = SnRotationMatrix.of(a, b);
      assertTrue(OrthogonalMatrixQ.of(rotation1, Chop._10));
      Chop._08.requireClose(rotation1.dot(a), b);
      Chop._08.requireClose(Det.of(rotation1), RealScalar.ONE);
      Chop._08.requireClose(rotation1, RotationMatrix3D.of(a, b));
    }
  }

  public void testTargetNDim() {
    for (int d = 2; d < 6; ++d)
      for (int count = 0; count < 10; ++count) {
        Tensor a = Vector2Norm.NORMALIZE.apply(RandomVariate.of(UNIFORM, d));
        Tensor b = Vector2Norm.NORMALIZE.apply(RandomVariate.of(UNIFORM, d));
        Tensor rotation1 = SnRotationMatrix.of(a, b);
        Tensor rotation2 = SnRotationMatrix.of(b, a);
        assertTrue(OrthogonalMatrixQ.of(rotation1, Chop._08));
        Chop._08.requireClose(new SnAction(rotation1).apply(a), b);
        Chop._08.requireClose(Det.of(rotation1), RealScalar.ONE);
        Chop._08.requireClose(rotation1, Inverse.of(rotation2));
      }
  }

  public void testTargetSn() {
    for (int d = 1; d < 6; ++d) {
      RandomSampleInterface randomSampleInterface = SnRandomSample.of(d);
      for (int count = 0; count < 10; ++count) {
        Tensor a = RandomSample.of(randomSampleInterface);
        Tensor b = RandomSample.of(randomSampleInterface);
        Tensor rotation1 = SnRotationMatrix.of(a, b);
        Tensor rotation2 = SnRotationMatrix.of(b, a);
        assertTrue(OrthogonalMatrixQ.of(rotation1, Chop._08));
        Chop._08.requireClose(rotation1.dot(a), b);
        Chop._08.requireClose(Det.of(rotation1), RealScalar.ONE);
        Chop._06.requireClose(rotation1, Inverse.of(rotation2));
      }
    }
  }
}

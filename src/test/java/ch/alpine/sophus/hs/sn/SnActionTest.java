// code by jph
package ch.alpine.sophus.hs.sn;

import ch.alpine.sophus.hs.r3s2.R3S2Geodesic;
import ch.alpine.sophus.math.sample.RandomSample;
import ch.alpine.sophus.math.sample.RandomSampleInterface;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.lie.Cross;
import ch.alpine.tensor.lie.TensorWedge;
import ch.alpine.tensor.mat.Inverse;
import ch.alpine.tensor.mat.OrthogonalMatrixQ;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.mat.re.Det;
import ch.alpine.tensor.nrm.Vector2Norm;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.NormalDistribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.sca.Chop;
import junit.framework.TestCase;

public class SnActionTest extends TestCase {
  private static final Distribution UNIFORM = NormalDistribution.standard();

  public void testSimple() {
    Scalar scalar = RealScalar.of(0.5);
    Tensor tensor = R3S2Geodesic.INSTANCE.split( //
        Tensors.fromString("{{0, 0, 0}, {1, 0, 0}}"), //
        Tensors.fromString("{{1, 0, 0}, {0, 1, 0}}"), scalar);
    Chop._11.requireClose(tensor, //
        Tensors.fromString("{{0.5, -0.20710678118654752, 0.0}, {0.7071067811865476, 0.7071067811865475, 0.0}}"));
  }

  public void testMatch3D() {
    for (int count = 0; count < 20; ++count) {
      Tensor a = Vector2Norm.NORMALIZE.apply(RandomVariate.of(UNIFORM, 3));
      Tensor b = Vector2Norm.NORMALIZE.apply(RandomVariate.of(UNIFORM, 3));
      Tensor w = Cross.of(a, b);
      Tensor wx = Cross.skew3(w);
      Tensor wd = TensorWedge.of(a, b).negate();
      Tolerance.CHOP.requireClose(wx, wd);
      Tensor rotation1 = SnManifold.INSTANCE.endomorphism(a, b);
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
        Tensor rotation1 = SnManifold.INSTANCE.endomorphism(a, b);
        Tensor rotation2 = SnManifold.INSTANCE.endomorphism(b, a);
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
        Tensor rotation1 = SnManifold.INSTANCE.endomorphism(a, b);
        Tensor rotation2 = SnManifold.INSTANCE.endomorphism(b, a);
        assertTrue(OrthogonalMatrixQ.of(rotation1, Chop._08));
        Chop._08.requireClose(rotation1.dot(a), b);
        Chop._08.requireClose(Det.of(rotation1), RealScalar.ONE);
        Chop._06.requireClose(rotation1, Inverse.of(rotation2));
      }
    }
  }
}
// code by jph
package ch.ethz.idsc.sophus.hs.sn;

import ch.ethz.idsc.sophus.hs.r3s2.R3S2Geodesic;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.Normalize;
import ch.ethz.idsc.tensor.lie.Cross;
import ch.ethz.idsc.tensor.lie.TensorWedge;
import ch.ethz.idsc.tensor.mat.Det;
import ch.ethz.idsc.tensor.mat.OrthogonalMatrixQ;
import ch.ethz.idsc.tensor.mat.Tolerance;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.NormalDistribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.red.Norm;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class SnRotationMatrixTest extends TestCase {
  private static final Distribution UNIFORM = NormalDistribution.standard();
  private static final TensorUnaryOperator NORMALIZE = Normalize.with(Norm._2);

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
      Tensor a = NORMALIZE.apply(RandomVariate.of(UNIFORM, 3));
      Tensor b = NORMALIZE.apply(RandomVariate.of(UNIFORM, 3));
      Tensor w = Cross.of(a, b);
      Tensor wx = Cross.skew3(w);
      Tensor wd = TensorWedge.of(a, b).multiply(RealScalar.of(-2));
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
      for (int count = 0; count < 20; ++count) {
        Tensor a = NORMALIZE.apply(RandomVariate.of(UNIFORM, d));
        Tensor b = NORMALIZE.apply(RandomVariate.of(UNIFORM, d));
        Tensor rotation1 = SnRotationMatrix.of(a, b);
        assertTrue(OrthogonalMatrixQ.of(rotation1, Chop._10));
        Chop._08.requireClose(rotation1.dot(a), b);
        Chop._08.requireClose(Det.of(rotation1), RealScalar.ONE);
      }
  }
}
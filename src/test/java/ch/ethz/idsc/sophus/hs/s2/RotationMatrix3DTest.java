// code by jph
package ch.ethz.idsc.sophus.hs.s2;

import ch.ethz.idsc.sophus.hs.r3s2.R3S2Geodesic;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.Normalize;
import ch.ethz.idsc.tensor.lie.Cross;
import ch.ethz.idsc.tensor.mat.Det;
import ch.ethz.idsc.tensor.mat.OrthogonalMatrixQ;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.pdf.UniformDistribution;
import ch.ethz.idsc.tensor.red.Norm;
import ch.ethz.idsc.tensor.sca.Chop;
import ch.ethz.idsc.tensor.sca.Clips;
import junit.framework.TestCase;

public class RotationMatrix3DTest extends TestCase {
  private static final Distribution UNIFORM = UniformDistribution.of(Clips.absoluteOne());
  private static final TensorUnaryOperator NORMALIZE = Normalize.with(Norm._2);

  public void testSimple() {
    Scalar scalar = RealScalar.of(0.5);
    Tensor tensor = R3S2Geodesic.INSTANCE.split( //
        Tensors.fromString("{{0, 0, 0}, {1, 0, 0}}"), //
        Tensors.fromString("{{1, 0, 0}, {0, 1, 0}}"), scalar);
    Chop._11.requireClose(tensor, //
        Tensors.fromString("{{0.5, -0.20710678118654752, 0.0}, {0.7071067811865476, 0.7071067811865475, 0.0}}"));
  }

  public void testTarget() {
    for (int count = 0; count < 20; ++count) {
      Tensor a = NORMALIZE.apply(RandomVariate.of(UNIFORM, 3));
      Tensor b = NORMALIZE.apply(RandomVariate.of(UNIFORM, 3));
      Tensor w = Cross.of(a, b);
      Tensor wx = Cross.skew3(w);
      Tensor rotation1 = RotationMatrix3D.of(a, b);
      assertTrue(OrthogonalMatrixQ.of(rotation1, Chop._10));
      Chop._08.requireClose(rotation1.dot(a), b);
      Chop._08.requireClose(Det.of(rotation1), RealScalar.ONE);
      // System.out.println(Pretty.of(TensorWedge.of(a, b).multiply(RealScalar.of(2)).map(Round._4)));
      // HodgeDual.of(tensor)
      // System.out.println(Pretty.of(wx.map(Round._4)));
      // Tensor rotation2 = MatrixExp.of(wx);
      // Chop._10.requireClose(rotation1, rotation2);
      // System.out.println("---");
    }
  }
}

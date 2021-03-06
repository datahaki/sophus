// code by jph
package ch.ethz.idsc.sophus.hs.r2;

import ch.ethz.idsc.sophus.gbc.AveragingWeights;
import ch.ethz.idsc.sophus.usr.AssertFail;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.lie.r2.RotationMatrix;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.NormalDistribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.pdf.UniformDistribution;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class Se2RigidMotionFitTest extends TestCase {
  public void testExact() {
    Distribution distribution = NormalDistribution.standard();
    Scalar angle = RealScalar.of(2);
    Tensor rotation = RotationMatrix.of(angle);
    for (int n = 5; n < 11; ++n) {
      Tensor points = RandomVariate.of(distribution, n, 2);
      Tensor transl = RandomVariate.of(distribution, 2);
      Tensor target = Tensor.of(points.stream().map(rotation::dot).map(transl::add));
      Tensor rigidMotionFit = Se2RigidMotionFit.of(points, target);
      Chop._10.requireClose(rigidMotionFit.Get(2), angle);
    }
  }

  public void testExact2() {
    Distribution distribution = NormalDistribution.standard();
    for (int n = 5; n < 11; ++n) {
      Tensor points = RandomVariate.of(distribution, n, 2);
      Tensor xya = RandomVariate.of(distribution, 3);
      Se2ForwardAction se2ForwardAction = new Se2ForwardAction(xya);
      Tensor target = Tensor.of(points.stream().map(se2ForwardAction::apply));
      Tensor rigidMotionFit = Se2RigidMotionFit.of(points, target);
      Chop._08.requireClose(xya, rigidMotionFit);
      Tensor rigidMotionFi2 = Se2RigidMotionFit.of(points, target, AveragingWeights.of(n));
      Chop._08.requireClose(xya, rigidMotionFi2);
    }
  }

  public void testWeights() {
    Distribution distribution = NormalDistribution.standard();
    Tensor points = RandomVariate.of(distribution, 10, 3);
    Tensor target = RandomVariate.of(distribution, 10, 3);
    AssertFail.of(() -> Se2RigidMotionFit.of(target, points));
    Tensor weights = RandomVariate.of(UniformDistribution.unit(), 10);
    AssertFail.of(() -> Se2RigidMotionFit.of(target, points, weights));
  }

  public void testEmptyFail() {
    AssertFail.of(() -> Se2RigidMotionFit.of(Tensors.empty(), Tensors.empty()));
    AssertFail.of(() -> Se2RigidMotionFit.of(Tensors.empty(), Tensors.empty(), Tensors.empty()));
  }
}

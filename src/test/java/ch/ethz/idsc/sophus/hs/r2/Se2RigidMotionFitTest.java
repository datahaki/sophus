// code by jph
package ch.ethz.idsc.sophus.hs.r2;

import ch.ethz.idsc.sophus.usr.AssertFail;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
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
    }
  }

  public void testWeights() {
    Distribution distribution = NormalDistribution.standard();
    for (int n = 5; n < 11; ++n) {
      Tensor weights = RandomVariate.of(UniformDistribution.unit(), 10);
      Tensor points = RandomVariate.of(distribution, 10, 3);
      Tensor target = RandomVariate.of(distribution, 10, 3);
      AssertFail.of(() -> Se2RigidMotionFit.of(target, points, weights));
    }
  }
}

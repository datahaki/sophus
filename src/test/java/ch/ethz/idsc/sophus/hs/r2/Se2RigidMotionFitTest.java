// code by jph
package ch.ethz.idsc.sophus.hs.r2;

import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.lie.RotationMatrix;
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
      Tensor target = Tensor.of(points.stream().map(p -> rotation.dot(p).add(transl)));
      Tensor rigidMotionFit = Se2RigidMotionFit.of(points, target);
      Chop._10.requireClose(rigidMotionFit.Get(2), angle);
    }
  }

  public void testWeights() {
    Distribution distribution = NormalDistribution.standard();
    for (int n = 5; n < 11; ++n) {
      Tensor weights = RandomVariate.of(UniformDistribution.unit(), 10);
      Tensor points = RandomVariate.of(distribution, 10, 3);
      Tensor target = RandomVariate.of(distribution, 10, 3);
      try {
        Se2RigidMotionFit.of(target, points, weights);
        fail();
      } catch (Exception exception) {
        // ---
      }
    }
  }
}

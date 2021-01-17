// code by jph
package ch.ethz.idsc.sophus.lie.se2c;

import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.Subdivide;
import ch.ethz.idsc.tensor.alg.Transpose;
import ch.ethz.idsc.tensor.mat.IdentityMatrix;
import ch.ethz.idsc.tensor.mat.Tolerance;
import ch.ethz.idsc.tensor.num.Pi;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class Se2SkewTest extends TestCase {
  public void testZero() {
    Tensor tensor = Se2Skew.logflow(RealScalar.ZERO);
    assertEquals(tensor, IdentityMatrix.of(2));
  }

  public void testFunctionM() {
    for (Tensor _angle : Subdivide.of(-4, 4, 20)) {
      Scalar angle = (Scalar) _angle;
      Tensor m1 = Se2Skew.logflow(angle);
      Tensor m2 = Se2Skew.logflow(angle.negate());
      Chop._10.requireClose(m1, Transpose.of(m2));
    }
  }

  public void testSingularity0() {
    Tensor tensor = Se2Skew.logflow(RealScalar.of(Double.MIN_VALUE));
    Chop._40.requireClose(tensor, IdentityMatrix.of(2));
  }

  public void testSingularityPi() {
    Tensor tensor = Se2Skew.logflow(Pi.VALUE);
    Tensor matrix = Tensors.matrix(new Scalar[][] { { RealScalar.ZERO, Pi.HALF }, { Pi.HALF.negate(), RealScalar.ZERO } });
    Chop._14.requireClose(tensor, matrix);
  }

  public void testSe2Exp() {
    Tensor xy = Tensors.vector(2, 3).unmodifiable();
    Scalar angle = RealScalar.of(0.2);
    Tensor csk = Se2Skew.logflow(angle);
    Tensor log = Se2CoveringExponential.INSTANCE.log(xy.copy().append(angle));
    Tolerance.CHOP.requireClose(log.extract(0, 2), csk.dot(xy));
  }
}

// code by jph
package ch.alpine.sophus.lie.se2c;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Subdivide;
import ch.alpine.tensor.alg.Transpose;
import ch.alpine.tensor.mat.IdentityMatrix;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.num.Pi;
import ch.alpine.tensor.sca.Chop;

class Se2SkewTest {
  @Test
  public void testZero() {
    Tensor tensor = Se2Skew.logflow(RealScalar.ZERO);
    assertEquals(tensor, IdentityMatrix.of(2));
  }

  @Test
  public void testFunctionM() {
    for (Tensor _angle : Subdivide.of(-4, 4, 20)) {
      Scalar angle = (Scalar) _angle;
      Tensor m1 = Se2Skew.logflow(angle);
      Tensor m2 = Se2Skew.logflow(angle.negate());
      Chop._10.requireClose(m1, Transpose.of(m2));
    }
  }

  @Test
  public void testSingularity0() {
    Tensor tensor = Se2Skew.logflow(RealScalar.of(Double.MIN_VALUE));
    Chop._40.requireClose(tensor, IdentityMatrix.of(2));
  }

  @Test
  public void testSingularityPi() {
    Tensor tensor = Se2Skew.logflow(Pi.VALUE);
    Tensor matrix = Tensors.matrix(new Scalar[][] { { RealScalar.ZERO, Pi.HALF }, { Pi.HALF.negate(), RealScalar.ZERO } });
    Chop._14.requireClose(tensor, matrix);
  }

  @Test
  public void testSe2Exp() {
    Tensor xy = Tensors.vector(2, 3).unmodifiable();
    Scalar angle = RealScalar.of(0.2);
    Tensor csk = Se2Skew.logflow(angle);
    Tensor log = Se2CoveringGroup.INSTANCE.log(xy.copy().append(angle));
    Tolerance.CHOP.requireClose(log.extract(0, 2), csk.dot(xy));
  }
}

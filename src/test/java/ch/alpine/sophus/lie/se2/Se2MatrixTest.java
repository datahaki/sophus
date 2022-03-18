// code by jph
package ch.alpine.sophus.lie.se2;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.lie.se2c.Se2CoveringExponential;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.mat.re.Det;
import ch.alpine.tensor.num.Pi;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.sca.Chop;

public class Se2MatrixTest {
  @Test
  public void testSimple1() {
    Tensor matrix = Se2Matrix.of(Tensors.vector(2, 3, 4));
    assertEquals(matrix.get(2), Tensors.vector(0, 0, 1));
    Scalar det = Det.of(matrix);
    Chop._14.requireClose(det, RealScalar.ONE);
  }

  @Test
  public void test2PiModLogExp() {
    for (int n = -5; n <= 5; ++n) {
      double value = 1 + 2 * Math.PI * n;
      Tensor g = Tensors.vector(2, 3, value);
      Tensor x = Se2CoveringExponential.INSTANCE.log(g);
      Tensor exp_x = Se2CoveringExponential.INSTANCE.exp(x);
      assertEquals(exp_x.Get(2), RealScalar.of(value));
      Chop._13.requireClose(g, exp_x);
    }
  }

  @Test
  public void test2PiModExpLog() {
    for (int n = -5; n <= 5; ++n) {
      double value = 1 + 2 * Math.PI * n;
      Tensor x = Tensors.vector(2, 3, value);
      Tensor g = Se2CoveringExponential.INSTANCE.exp(x);
      Tensor log_g = Se2CoveringExponential.INSTANCE.log(g);
      Chop._13.requireClose(x, log_g);
    }
  }

  @Test
  public void testLog() {
    Distribution distribution = UniformDistribution.of(-25, 25);
    for (int index = 0; index < 10; ++index) {
      Tensor x = RandomVariate.of(distribution, 3);
      Tensor g = Se2CoveringExponential.INSTANCE.exp(x);
      Tensor log_g = Se2CoveringExponential.INSTANCE.log(g);
      Chop._10.requireClose(x, log_g);
    }
  }

  @Test
  public void testExp() {
    Distribution distribution = UniformDistribution.of(-25, 25);
    for (int index = 0; index < 10; ++index) {
      Tensor g = RandomVariate.of(distribution, 3);
      Tensor x = Se2CoveringExponential.INSTANCE.log(g);
      Tensor exp_x = Se2CoveringExponential.INSTANCE.exp(x);
      Chop._10.requireClose(g, exp_x);
    }
  }

  @Test
  public void testLog0() {
    Distribution distribution = UniformDistribution.of(-5, 5);
    for (int index = 0; index < 10; ++index) {
      Tensor x = RandomVariate.of(distribution, 2).append(RealScalar.ZERO);
      Tensor g0 = Se2CoveringExponential.INSTANCE.exp(x);
      Tensor x2 = Se2CoveringExponential.INSTANCE.log(g0);
      Chop._13.requireClose(x, x2);
    }
  }

  @Test
  public void testFromMatrix() {
    Tensor x = Tensors.vector(2, 3, .5);
    Tensor matrix = Se2Matrix.of(x);
    Tensor y = Se2Matrix.toVector(matrix);
    assertEquals(x, y);
  }

  @Test
  public void testFromMatrix1() {
    Tensor x = Tensors.vector(2, 3, 3.5);
    Tensor matrix = Se2Matrix.of(x);
    Tensor y = Se2Matrix.toVector(matrix);
    Chop._10.requireClose(x.Get(2), y.Get(2).add(Pi.TWO));
  }

  @Test
  public void testG0() {
    Tensor u = Tensors.vector(1.2, 0, 0);
    Tensor m = Se2CoveringExponential.INSTANCE.exp(u);
    assertEquals(m, u);
  }
}

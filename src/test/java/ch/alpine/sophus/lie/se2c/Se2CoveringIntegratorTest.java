// code by jph
package ch.alpine.sophus.lie.se2c;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.lie.se2.Se2Matrix;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Array;
import ch.alpine.tensor.alg.Subdivide;
import ch.alpine.tensor.mat.ex.MatrixExp;
import ch.alpine.tensor.num.Pi;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.sca.Chop;

class Se2CoveringIntegratorTest {
  @Test
  void testFullRotation() {
    Tensor g = Tensors.vector(10, 0, 0).unmodifiable();
    for (Tensor _x : Subdivide.of(-2, 10, 72)) {
      Tensor x = Tensors.of(_x, RealScalar.ZERO, Pi.TWO);
      Tensor r = Se2CoveringIntegrator.INSTANCE.spin(g, x);
      Chop._12.requireClose(r, Tensors.vector(10.0, 0.0, 6.283185307179586));
    }
  }

  private static Tensor exp_of(Scalar x, Scalar y, Scalar theta) {
    Tensor matrix = Array.zeros(3, 3);
    matrix.set(theta, 1, 0);
    matrix.set(theta.negate(), 0, 1);
    matrix.set(x, 0, 2);
    matrix.set(y, 1, 2);
    return MatrixExp.of(matrix);
  }

  private static Tensor exp_of(Number x, Number y, Number theta) {
    return exp_of(RealScalar.of(x), RealScalar.of(y), RealScalar.of(theta));
  }

  @Test
  void testExpSubstitute() {
    Tensor mat = exp_of(1, 2, .3);
    Tensor vec = Se2CoveringIntegrator.INSTANCE.spin(Array.zeros(3), Tensors.vector(1, 2, .3));
    Tensor v0 = Se2CoveringGroup.INSTANCE.exp(Tensors.vector(1, 2, .3));
    assertEquals(vec, v0);
    Tensor alt = Se2Matrix.of(vec);
    Chop._13.requireClose(mat, alt);
  }

  @Test
  void testExpSubstitute2() {
    for (int index = 0; index < 20; ++index) {
      Tensor rnd = RandomVariate.of(NormalDistribution.standard(), 3);
      Tensor mat = exp_of(rnd.Get(0), rnd.Get(1), rnd.Get(2));
      Tensor vec = Se2CoveringIntegrator.INSTANCE.spin(Array.zeros(3), rnd);
      Tensor v0 = Se2CoveringGroup.INSTANCE.exp(rnd);
      assertEquals(vec, v0);
      Tensor alt = Se2Matrix.of(vec);
      boolean close = Chop._11.isClose(mat, alt);
      if (!close) {
        System.err.println(alt);
        System.err.println(mat);
      }
      assertTrue(close);
    }
  }

  @Test
  void testUnits() {
    Tensor spin = Se2CoveringIntegrator.INSTANCE.spin( //
        Tensors.fromString("{1[m], 2[m], 3}"), //
        Tensors.fromString("{0.4[m], -0.3[m], 0.7}"));
    Tensor expected = Tensors.fromString("{0.5557854299223493[m], 2.2064712267635618[m], 3.7}");
    Chop._13.requireClose(spin, expected);
  }
}

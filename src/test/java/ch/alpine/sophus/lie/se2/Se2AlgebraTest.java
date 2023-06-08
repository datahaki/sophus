// code by jph
package ch.alpine.sophus.lie.se2;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.hs.Exponential;
import ch.alpine.sophus.hs.ad.HsAlgebra;
import ch.alpine.sophus.lie.MatrixAlgebra;
import ch.alpine.sophus.lie.se2c.Se2CoveringGroup;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.chq.ExactTensorQ;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.sca.Chop;
import ch.alpine.tensor.spa.Normal;

class Se2AlgebraTest {
  @Test
  void testFromMatrices() {
    MatrixAlgebra matrixAlgebra = new MatrixAlgebra(Se2Algebra.basis());
    Tensor ad = Se2Algebra.INSTANCE.ad();
    assertEquals(ad, matrixAlgebra.ad());
    assertEquals(ad, Normal.of(matrixAlgebra.ad()));
    assertEquals(ad, Se2Algebra.INSTANCE.ad());
  }

  @Test
  void testSimple() {
    Tensor ad = Tensors.fromString( //
        "{{{0, 0, 0}, {0, 0, -1}, {0, 1, 0}}, {{0, 0, 1}, {0, 0, 0}, {-1, 0, 0}}, {{0, 0, 0}, {0, 0, 0}, {0, 0, 0}}}");
    ExactTensorQ.require(Se2Algebra.INSTANCE.ad());
    assertEquals(Se2Algebra.INSTANCE.ad(), ad);
  }

  @Test
  void testSe2ExpExpLog() {
    Distribution distribution = UniformDistribution.of(-0.1, 0.1);
    Tensor x = RandomVariate.of(distribution, 3);
    Tensor y = RandomVariate.of(distribution, 3);
    Exponential exponential = Se2CoveringGroup.INSTANCE;
    Tensor mX = exponential.exp(x);
    Tensor mY = exponential.exp(y);
    Tensor res = exponential.log(Se2CoveringGroup.INSTANCE.element(mX).combine(mY));
    Tensor z = Se2Algebra.INSTANCE.bch(6).apply(x, y);
    Chop._06.requireClose(z, res);
  }

  @Test
  void testSe2Log() {
    Distribution distribution = UniformDistribution.of(-0.1, 0.1);
    Tensor x = RandomVariate.of(distribution, 3);
    Tensor y = RandomVariate.of(distribution, 3);
    MatrixAlgebra matrixAlgebra = new MatrixAlgebra(Se2Algebra.basis());
    matrixAlgebra.ad().dot(x).dot(y);
    // Tensor log = MatrixLog.of(matrixAlgebra.toMatrix(x));
    // System.out.println(x);
    // System.out.println(Pretty.of(log.map(Round._4)));
  }

  @Test
  void testHsFails() {
    assertThrows(Exception.class, () -> new HsAlgebra(Se2Algebra.INSTANCE.ad(), 1, 6));
  }
}

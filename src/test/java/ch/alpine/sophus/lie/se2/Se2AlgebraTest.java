// code by jph
package ch.alpine.sophus.lie.se2;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.hs.Exponential;
import ch.alpine.sophus.hs.HsAlgebra;
import ch.alpine.sophus.lie.LieAlgebraAds;
import ch.alpine.sophus.lie.MatrixAlgebra;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.api.TensorBinaryOperator;
import ch.alpine.tensor.chq.ExactTensorQ;
import ch.alpine.tensor.lie.bch.BakerCampbellHausdorff;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.sca.Chop;
import ch.alpine.tensor.spa.Normal;

class Se2AlgebraTest {
  @Test
  void testFromMatrices() {
    MatrixAlgebra matrixAlgebra = new MatrixAlgebra(Se2CoveringGroup.INSTANCE.matrixBasis());
    Tensor ad = LieAlgebraAds.se(2);
    assertEquals(ad, matrixAlgebra.ad());
    assertEquals(ad, Normal.of(matrixAlgebra.ad()));
  }

  @Test
  void testSimple() {
    Tensor ad = Tensors.fromString( //
        "{{{0, 0, 0}, {0, 0, -1}, {0, 1, 0}}, {{0, 0, 1}, {0, 0, 0}, {-1, 0, 0}}, {{0, 0, 0}, {0, 0, 0}, {0, 0, 0}}}");
    ExactTensorQ.require(LieAlgebraAds.se(2));
    assertEquals(LieAlgebraAds.se(2), ad);
  }

  @Test
  void testSe2ExpExpLog() {
    Distribution distribution = UniformDistribution.of(-0.1, 0.1);
    Tensor x = RandomVariate.of(distribution, 3);
    Tensor y = RandomVariate.of(distribution, 3);
    Exponential exponential = Se2CoveringGroup.INSTANCE.exponential0();
    Tensor mX = exponential.exp(x);
    Tensor mY = exponential.exp(y);
    Tensor res = exponential.log(Se2CoveringGroup.INSTANCE.combine(mX, mY));
    TensorBinaryOperator bch = BakerCampbellHausdorff.of(LieAlgebraAds.se(2), 6);
    Tensor z = bch.apply(x, y);
    Chop._06.requireClose(z, res);
  }

  @Test
  void testHsFails() {
    assertThrows(Exception.class, () -> new HsAlgebra(LieAlgebraAds.se(2), 1, 6));
  }
}

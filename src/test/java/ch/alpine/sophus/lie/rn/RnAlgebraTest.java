// code by jph
package ch.alpine.sophus.lie.rn;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.lie.MatrixAlgebra;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.TensorBinaryOperator;
import ch.alpine.tensor.lie.bch.BakerCampbellHausdorff;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;

class RnAlgebraTest {
  @Test
  void testSimple() {
    RnAlgebra rnAlgebra = new RnAlgebra(3);
    TensorBinaryOperator bch1 = BakerCampbellHausdorff.of(rnAlgebra.ad(), 2);
    Tensor x = RandomVariate.of(NormalDistribution.standard(), 3);
    Tensor y = RandomVariate.of(NormalDistribution.standard(), 3);
    assertEquals(bch1.apply(x, y), x.add(y));
  }

  @Test
  void testConsistency() {
    RnAlgebra rnAlgebra = new RnAlgebra(3);
    Tensor basis = RnAlgebra.basis(3);
    assertEquals(rnAlgebra.ad(), new MatrixAlgebra(basis).ad());
  }
}

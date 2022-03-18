// code by jph
package ch.alpine.sophus.lie.rn;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.function.BinaryOperator;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.lie.MatrixAlgebra;
import ch.alpine.sophus.math.bch.BakerCampbellHausdorff;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;

public class RnAlgebraTest {
  @Test
  public void testSimple() {
    RnAlgebra rnAlgebra = new RnAlgebra(3);
    BinaryOperator<Tensor> bch1 = BakerCampbellHausdorff.of(rnAlgebra.ad(), 2);
    BinaryOperator<Tensor> bch2 = rnAlgebra.bch(6);
    Tensor x = RandomVariate.of(NormalDistribution.standard(), 3);
    Tensor y = RandomVariate.of(NormalDistribution.standard(), 3);
    assertEquals(bch1.apply(x, y), bch2.apply(x, y));
  }

  @Test
  public void testConsistency() {
    RnAlgebra rnAlgebra = new RnAlgebra(3);
    Tensor basis = rnAlgebra.basis();
    assertEquals(rnAlgebra.ad(), new MatrixAlgebra(basis).ad());
  }
}

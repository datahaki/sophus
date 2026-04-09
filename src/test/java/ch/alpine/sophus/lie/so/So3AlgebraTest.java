// code by jph
package ch.alpine.sophus.lie.so;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.hs.HsAlgebra;
import ch.alpine.sophus.lie.MatrixAlgebra;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.UnitVector;
import ch.alpine.tensor.api.TensorBinaryOperator;
import ch.alpine.tensor.lie.BakerCampbellHausdorff;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.sca.Chop;

class So3AlgebraTest {
  @Test
  void testSo3H() {
    Tensor basis = MatrixAlgebra.of(So3Group.INSTANCE).basis();
    MatrixAlgebra matrixAlgebra = new MatrixAlgebra(basis);
    Tensor so3 = matrixAlgebra.ad();
    Distribution distribution = UniformDistribution.of(-0.05, 0.05);
    Tensor g = RandomVariate.of(distribution, 3);
    Tensor h = UnitVector.of(3, 2).multiply(RandomVariate.of(distribution));
    TensorBinaryOperator bch = BakerCampbellHausdorff.of(so3, 6);
    HsAlgebra hsAlgebra = new HsAlgebra(so3, 2, 8);
    Tensor prj_g = hsAlgebra.projection(g);
    Tensor res = bch.apply(g, h);
    Tensor prj_gh = hsAlgebra.projection(res);
    Chop._10.requireClose(prj_g, prj_gh);
  }
}

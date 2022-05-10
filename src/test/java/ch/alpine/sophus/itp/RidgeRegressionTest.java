// code by jph
package ch.alpine.sophus.itp;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.api.TensorMapping;
import ch.alpine.sophus.hs.VectorLogManifold;
import ch.alpine.sophus.itp.RidgeRegression.Form2;
import ch.alpine.sophus.lie.LieGroupOps;
import ch.alpine.sophus.lie.se2c.Se2CoveringGroup;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.mat.PositiveDefiniteMatrixQ;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.UniformDistribution;

class RidgeRegressionTest {
  private static final LieGroupOps LIE_GROUP_OPS = new LieGroupOps(Se2CoveringGroup.INSTANCE);

  @Test
  public void testSe2C() {
    Distribution distribution = UniformDistribution.of(-10, +10);
    VectorLogManifold vectorLogManifold = Se2CoveringGroup.INSTANCE;
    RidgeRegression ridgeRegression = new RidgeRegression(vectorLogManifold);
    for (int count = 4; count < 10; ++count) {
      Tensor sequence = RandomVariate.of(distribution, count, 3);
      for (Tensor point : sequence) {
        Form2 form2 = ridgeRegression.new Form2(sequence, point);
        Tensor sigma_inverse = form2.sigma_inverse();
        assertTrue(PositiveDefiniteMatrixQ.ofHermitian(sigma_inverse));
      }
      {
        Tensor point = RandomVariate.of(distribution, 3);
        ridgeRegression.new Form2(sequence, point).leverages();
        Tensor shift = RandomVariate.of(distribution, 3);
        for (TensorMapping tensorMapping : LIE_GROUP_OPS.biinvariant(shift)) {
          Tensor all = tensorMapping.slash(sequence);
          Tensor one = tensorMapping.apply(point);
          ridgeRegression.new Form2(all, one).leverages();
          // System.out.println(Chop._05.close(l1, l2));
        }
      }
    }
  }
}

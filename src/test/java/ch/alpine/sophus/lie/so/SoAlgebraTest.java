// code by jph
package ch.alpine.sophus.lie.so;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.hs.HsAlgebra;
import ch.alpine.sophus.lie.HigherJacobiIdentity;
import ch.alpine.sophus.lie.LieAlgebra;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Dimensions;
import ch.alpine.tensor.mat.AntisymmetricMatrixQ;

class SoAlgebraTest {
  @Test
  void testHsS3() {
    for (int n = 2; n < 7; ++n) {
      LieAlgebra lieAlgebra = SoAlgebra.of(n);
      Tensor ad = lieAlgebra.ad();
      HsAlgebra hsAlgebra = new HsAlgebra(ad, ad.length() - 1, 6);
      assertTrue(hsAlgebra.isReductive());
      if (2 < n)
        assertFalse(hsAlgebra.isHTrivial());
      SoAlgebra.basis(n).forEach(AntisymmetricMatrixQ.INSTANCE::requireMember);
      HigherJacobiIdentity.of4(ad);
      HigherJacobiIdentity.of4b(ad);
      HigherJacobiIdentity.of5(ad);
      HigherJacobiIdentity.of5b(ad);
    }
  }

  @Test
  void testMatch() {
    for (int n = 2; n < 7; ++n) {
      Tensor b1 = SoAlgebra.basis(n);
      Tensor b2 = new SoNGroup(n).matrixBasis();
      assertEquals(Dimensions.of(b1), Dimensions.of(b2));
    }
  }
}

// code by jph
package ch.alpine.sophus.lie.so;

import ch.alpine.sophus.hs.HsAlgebra;
import ch.alpine.sophus.lie.LieAlgebra;
import ch.alpine.tensor.Tensor;
import junit.framework.TestCase;

public class SoAlgebraTest extends TestCase {
  public void testHsS3() {
    for (int n = 2; n < 7; ++n) {
      LieAlgebra lieAlgebra = SoAlgebra.of(n);
      Tensor ad = lieAlgebra.ad();
      HsAlgebra hsAlgebra = new HsAlgebra(ad, ad.length() - 1, 6);
      assertTrue(hsAlgebra.isReductive());
      if (2 < n)
        assertFalse(hsAlgebra.isHTrivial());
    }
  }
}

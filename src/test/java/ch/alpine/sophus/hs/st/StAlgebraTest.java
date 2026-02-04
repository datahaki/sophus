// code by jph
package ch.alpine.sophus.hs.st;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.hs.HsAdGeodesic;
import ch.alpine.sophus.hs.HsAlgebra;
import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.TriangularDistribution;

class StAlgebraTest {
  @Test
  void test5x3() {
    HsAlgebra hsAlgebra = StAlgebra.of(5, 3, 8);
    assertFalse(hsAlgebra.isHTrivial());
    assertEquals(hsAlgebra.dimM(), 9);
    assertEquals(hsAlgebra.dimG(), 10);
  }

  @Test
  void test5x2Geodesic() {
    HsAlgebra hsAlgebra = StAlgebra.of(5, 2, 8);
    assertFalse(hsAlgebra.isHTrivial());
    assertEquals(hsAlgebra.dimM(), 7);
    assertEquals(hsAlgebra.dimG(), 10);
    HsAdGeodesic hsAdGeodesic = new HsAdGeodesic(hsAlgebra);
    Distribution distribution = TriangularDistribution.with(0, 0.1);
    Tensor x = RandomVariate.of(distribution, 7);
    Tensor y = RandomVariate.of(distribution, 7);
    hsAdGeodesic.split(x, y, RationalScalar.of(1, 3));
  }

  @Test
  void testInteresting() {
    StAlgebra.of(5, 5, 8);
    assertThrows(Exception.class, () -> StAlgebra.of(5, 6, 8));
  }

  @Test
  void testFails() {
    assertThrows(Exception.class, () -> StAlgebra.of(5, -1, 8));
  }
}

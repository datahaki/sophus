// code by jph
package ch.alpine.sophus.hs.gr;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.hs.HsAlgebra;
import ch.alpine.tensor.spa.SparseArray;

class GrAlgebraTest {
  @Test
  void testLarge() {
    HsAlgebra hsAlgebra = GrAlgebra.of(7, 3, 6);
    assertEquals(hsAlgebra.dimG(), 7 * 6 / 2);
  }

  @Test
  void testBasis() {
    assertInstanceOf(SparseArray.class, GrAlgebra.basis(5, 2));
  }
}

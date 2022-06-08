// code by jph
package ch.alpine.sophus.lie.se3;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.hs.ad.HsAlgebra;

class Se3AlgebraTest {
  @Test
  public void testSimple() {
    Se3Algebra.INSTANCE.basis();
    // basis2.stream().forEach(basis->System.out.println(Pretty.of(basis)));
    Se3Algebra.INSTANCE.bch(3);
  }

  @Test
  public void testHsFails() {
    assertThrows(Exception.class, () -> new HsAlgebra(Se3Algebra.INSTANCE.ad(), 1, 6));
    assertThrows(Exception.class, () -> new HsAlgebra(Se3Algebra.INSTANCE.ad(), 2, 6));
    assertEquals(new HsAlgebra(Se3Algebra.INSTANCE.ad(), 3, 6).dimH(), 3);
    assertThrows(Exception.class, () -> new HsAlgebra(Se3Algebra.INSTANCE.ad(), 4, 6));
  }
}

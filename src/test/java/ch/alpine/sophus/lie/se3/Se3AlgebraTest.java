// code by jph
package ch.alpine.sophus.lie.se3;

import ch.alpine.sophus.hs.ad.HsAlgebra;
import ch.alpine.sophus.usr.AssertFail;
import junit.framework.TestCase;

public class Se3AlgebraTest extends TestCase {
  public void testSimple() {
    Se3Algebra.INSTANCE.basis();
    // basis2.stream().forEach(basis->System.out.println(Pretty.of(basis)));
    Se3Algebra.INSTANCE.bch(3);
  }

  public void testHsFails() {
    AssertFail.of(() -> new HsAlgebra(Se3Algebra.INSTANCE.ad(), 1, 6));
    AssertFail.of(() -> new HsAlgebra(Se3Algebra.INSTANCE.ad(), 2, 6));
    assertEquals(new HsAlgebra(Se3Algebra.INSTANCE.ad(), 3, 6).dimH(), 3);
    AssertFail.of(() -> new HsAlgebra(Se3Algebra.INSTANCE.ad(), 4, 6));
  }
}

// code by jph
package ch.alpine.sophus.lie.se3;

import junit.framework.TestCase;

public class Se3AlgebraTest extends TestCase {
  public void testSimple() {
    Se3Algebra.INSTANCE.basis();
    // basis2.stream().forEach(basis->System.out.println(Pretty.of(basis)));
    Se3Algebra.INSTANCE.bch(3);
  }
}

// code by jph
package ch.alpine.sophus.lie;

import ch.alpine.sophus.lie.se2.Se2Algebra;
import ch.alpine.sophus.usr.AssertFail;
import ch.alpine.tensor.lie.ad.MatrixAlgebra;
import ch.alpine.tensor.spa.Normal;
import junit.framework.TestCase;

public class HsAlgebraTest extends TestCase {
  public void testSimple() {
    MatrixAlgebra matrixAlgebra = new MatrixAlgebra(LieAlgebras.se2_basis());
    assertEquals(LieAlgebras.se2(), matrixAlgebra.ad());
    assertEquals(LieAlgebras.se2(), Normal.of(matrixAlgebra.ad()));
    assertEquals(LieAlgebras.se2(), Se2Algebra.INSTANCE.ad());
  }

  public void testSe2() {
    HsAlgebra hsAlgebra = new HsAlgebra(LieAlgebras.se2(), 2);
    assertTrue(hsAlgebra.isReductive());
    // assertFalse(hsAlgebra.isSymmetric());
  }

  public void testSe2Fail() {
    AssertFail.of(() -> new HsAlgebra(LieAlgebras.se2(), 1));
  }

  public void testSo3() {
    HsAlgebra hsAlgebra = new HsAlgebra(LieAlgebras.so3(), 2);
    assertTrue(hsAlgebra.isReductive());
    assertTrue(hsAlgebra.isSymmetric());
  }

  public void testSl2() {
    MatrixAlgebra matrixAlgebra = new MatrixAlgebra(LieAlgebras.sl2_basis_mh());
    HsAlgebra hsAlgebra = new HsAlgebra(matrixAlgebra.ad(), 2);
    assertEquals(hsAlgebra.dimG(), 3);
    assertEquals(hsAlgebra.dimM(), 2);
    assertEquals(hsAlgebra.dimH(), 1);
    assertTrue(hsAlgebra.isReductive());
    assertTrue(hsAlgebra.isSymmetric());
    hsAlgebra.printTable();
  }
}

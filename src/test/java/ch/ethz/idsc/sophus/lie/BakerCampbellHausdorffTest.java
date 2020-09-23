// code by jph
package ch.ethz.idsc.sophus.lie;

import java.util.Arrays;

import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.Array;
import ch.ethz.idsc.tensor.alg.UnitVector;
import ch.ethz.idsc.tensor.qty.Boole;
import junit.framework.TestCase;

public class BakerCampbellHausdorffTest extends TestCase {
  public void testSimple() {
    Tensor b0 = Array.of(l -> Boole.of(l.equals(Arrays.asList(0, 1))), 3, 3);
    Tensor b1 = Array.of(l -> Boole.of(l.equals(Arrays.asList(1, 2))), 3, 3);
    Tensor b2 = Array.of(l -> Boole.of(l.equals(Arrays.asList(0, 2))), 3, 3);
    Tensor basis = Tensors.of(b0, b1, b2);
    Tensor ad = LieAlgebras.he1();
    int n = ad.length();
    for (int c0 = 0; c0 < n; ++c0)
      for (int c1 = 0; c1 < n; ++c1) {
        Tensor mr = MatrixBracket.of(basis.get(c0), basis.get(c1));
        Tensor ar = ad.dot(UnitVector.of(n, c0)).dot(UnitVector.of(n, c1));
        assertEquals(ar.dot(basis), mr);
      }
  }

  private static void _check(int degree, Tensor ad) {
    int n = ad.length();
    for (int c0 = 0; c0 < n; ++c0)
      for (int c1 = 0; c1 < n; ++c1) {
        Tensor x = UnitVector.of(3, c0);
        Tensor y = UnitVector.of(3, c1);
        Tensor res1 = BakerCampbellHausdorff.of(ad, x, y, degree);
        Tensor res2 = BakerCampbellHausdorff.ap(degree, ad, x, y);
        assertEquals(res1, res2);
      }
  }

  public void testHe1() {
    _check(0, LieAlgebras.he1());
    _check(1, LieAlgebras.he1());
    _check(2, LieAlgebras.he1());
    _check(3, LieAlgebras.he1());
  }

  public void testSe2() {
    _check(0, LieAlgebras.se2());
    _check(1, LieAlgebras.se2());
    // _check(2, LieAlgebras.se2());
  }

  public void testSo3() {
    _check(0, LieAlgebras.so3());
    _check(1, LieAlgebras.so3());
    // _check(2, LieAlgebras.so3());
  }
}

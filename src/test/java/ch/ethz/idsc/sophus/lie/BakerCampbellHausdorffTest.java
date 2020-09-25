// code by jph
package ch.ethz.idsc.sophus.lie;

import java.util.Arrays;

import ch.ethz.idsc.sophus.lie.so3.Rodrigues;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Scalars;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.Array;
import ch.ethz.idsc.tensor.alg.UnitVector;
import ch.ethz.idsc.tensor.lie.JacobiIdentity;
import ch.ethz.idsc.tensor.lie.LeviCivitaTensor;
import ch.ethz.idsc.tensor.qty.Boole;
import ch.ethz.idsc.tensor.red.Norm;
import ch.ethz.idsc.tensor.sca.Chop;
import ch.ethz.idsc.tensor.sca.N;
import junit.framework.TestCase;

public class BakerCampbellHausdorffTest extends TestCase {
  private static void _check(Tensor ad, Tensor basis) {
    JacobiIdentity.require(ad);
    int n = ad.length();
    assertEquals(n, basis.length());
    for (int c0 = 0; c0 < n; ++c0)
      for (int c1 = 0; c1 < n; ++c1) {
        Tensor mr = MatrixBracket.of(basis.get(c0), basis.get(c1));
        Tensor ar = ad.dot(UnitVector.of(n, c0)).dot(UnitVector.of(n, c1));
        assertEquals(ar.dot(basis), mr);
      }
    assertEquals(AdBuilder.of(basis), ad);
  }

  public void testHe1Basis() {
    Tensor b0 = Array.of(l -> Boole.of(l.equals(Arrays.asList(0, 1))), 3, 3);
    Tensor b1 = Array.of(l -> Boole.of(l.equals(Arrays.asList(1, 2))), 3, 3);
    Tensor b2 = Array.of(l -> Boole.of(l.equals(Arrays.asList(0, 2))), 3, 3);
    Tensor basis = Tensors.of(b0, b1, b2);
    _check(LieAlgebras.he1(), basis);
  }

  public void testSo3Basis() {
    Tensor basis = LeviCivitaTensor.of(3).negate();
    _check(LieAlgebras.so3(), basis);
  }

  public void testSe2Basis() {
    Tensor b0 = Tensors.fromString("{{0, 0, 1}, {0, 0, 0}, {0, 0, 0}}");
    Tensor b1 = Tensors.fromString("{{0, 0, 0}, {0, 0, 1}, {0, 0, 0}}");
    Tensor b2 = LeviCivitaTensor.of(3).get(2).negate();
    Tensor basis = Tensors.of(b0, b1, b2);
    _check(LieAlgebras.se2(), basis);
  }

  private static void _check(int degree, Tensor ad) {
    int n = ad.length();
    for (int c0 = 0; c0 < n; ++c0)
      for (int c1 = 0; c1 < n; ++c1) {
        Tensor x = UnitVector.of(3, c0);
        Tensor y = UnitVector.of(3, c1);
        Tensor res1 = BakerCampbellHausdorff.of(ad, x, y, degree);
        Tensor res2 = BchApprox.of(degree, ad, x, y);
        assertEquals(res1, res2);
      }
  }

  public void testConv() {
    Tensor x = Tensors.vector(0.1, 0.2, 0.05);
    Tensor y = Tensors.vector(0.02, -0.1, -0.04);
    Tensor mX = Rodrigues.vectorExp(x);
    Tensor mY = Rodrigues.vectorExp(y);
    Tensor res = Rodrigues.INSTANCE.vectorLog(mX.dot(mY));
    Tensor ad = N.DOUBLE.of(LieAlgebras.so3());
    Scalar cmp = RealScalar.ONE;
    for (int degree = 0; degree < 6; ++degree) {
      Tensor z = BakerCampbellHausdorff.of(ad, x, y, degree);
      Scalar err = Norm._2.between(res, z);
      assertTrue(Scalars.lessThan(err, cmp));
      cmp = err;
    }
    Chop._08.requireZero(cmp);
  }

  public void testHe1() {
    _check(0, LieAlgebras.he1());
    _check(1, LieAlgebras.he1());
    _check(2, LieAlgebras.he1());
    _check(3, LieAlgebras.he1());
  }

  public void testSl2() {
    _check(0, LieAlgebras.sl2());
    _check(1, LieAlgebras.sl2());
    _check(2, LieAlgebras.sl2());
    _check(3, LieAlgebras.sl2());
  }

  public void testSe2() {
    _check(0, LieAlgebras.se2());
    _check(1, LieAlgebras.se2());
    _check(2, LieAlgebras.se2());
    _check(3, LieAlgebras.se2());
  }

  public void testSo3() {
    _check(0, LieAlgebras.so3());
    _check(1, LieAlgebras.so3());
    _check(2, LieAlgebras.so3());
    _check(3, LieAlgebras.so3());
  }
}

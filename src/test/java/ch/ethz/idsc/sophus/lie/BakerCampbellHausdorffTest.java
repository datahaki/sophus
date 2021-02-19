// code by jph
package ch.ethz.idsc.sophus.lie;

import java.util.Arrays;
import java.util.function.BinaryOperator;

import ch.ethz.idsc.sophus.lie.se2c.Se2CoveringExponential;
import ch.ethz.idsc.sophus.lie.se2c.Se2CoveringGroup;
import ch.ethz.idsc.sophus.lie.so3.Rodrigues;
import ch.ethz.idsc.sophus.usr.AssertFail;
import ch.ethz.idsc.tensor.ExactTensorQ;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Scalars;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.Array;
import ch.ethz.idsc.tensor.alg.UnitVector;
import ch.ethz.idsc.tensor.lie.LeviCivitaTensor;
import ch.ethz.idsc.tensor.nrm.Vector2Norm;
import ch.ethz.idsc.tensor.pdf.DiscreteUniformDistribution;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.red.KroneckerDelta;
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
    Tensor b0 = Array.of(l -> KroneckerDelta.of(l, Arrays.asList(0, 1)), 3, 3);
    Tensor b1 = Array.of(l -> KroneckerDelta.of(l, Arrays.asList(1, 2)), 3, 3);
    Tensor b2 = Array.of(l -> KroneckerDelta.of(l, Arrays.asList(0, 2)), 3, 3);
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

  private static void _check(Tensor ad) {
    BakerCampbellHausdorff bakerCampbellHausdorff = //
        (BakerCampbellHausdorff) BakerCampbellHausdorff.of(ad, BchApprox.DEGREE);
    BchApprox appx = (BchApprox) BchApprox.of(ad);
    int n = ad.length();
    for (int c0 = 0; c0 < n; ++c0)
      for (int c1 = 0; c1 < n; ++c1) {
        Tensor x = UnitVector.of(3, c0);
        Tensor y = UnitVector.of(3, c1);
        {
          Tensor res1 = bakerCampbellHausdorff.apply(x, y);
          Tensor res2 = appx.apply(x, y);
          assertEquals(res1, res2);
          ExactTensorQ.require(res1);
        }
        {
          Tensor res1 = bakerCampbellHausdorff.series(x, y);
          Tensor res2 = appx.series(x, y);
          assertEquals(res1, res2);
          ExactTensorQ.require(res1);
        }
      }
    Distribution distribution = DiscreteUniformDistribution.of(-10, 10);
    for (int count = 0; count < 10; ++count) {
      Tensor x = RandomVariate.of(distribution, 3);
      Tensor y = RandomVariate.of(distribution, 3);
      {
        Tensor res1 = bakerCampbellHausdorff.apply(x, y);
        Tensor res2 = appx.apply(x, y);
        assertEquals(res1, res2);
        ExactTensorQ.require(res1);
        Tensor res3 = bakerCampbellHausdorff.apply(y.negate(), x.negate()).negate();
        assertEquals(res1, res3);
      }
    }
  }

  public void testConvergenceSo3() {
    Tensor x = Tensors.vector(0.1, 0.2, 0.05);
    Tensor y = Tensors.vector(0.02, -0.1, -0.04);
    Tensor mX = Rodrigues.vectorExp(x);
    Tensor mY = Rodrigues.vectorExp(y);
    Tensor res = Rodrigues.INSTANCE.vectorLog(mX.dot(mY));
    Tensor ad = N.DOUBLE.of(LieAlgebras.so3());
    Scalar cmp = RealScalar.ONE;
    for (int degree = 1; degree < 6; ++degree) {
      BinaryOperator<Tensor> binaryOperator = BakerCampbellHausdorff.of(ad, degree);
      Tensor z = binaryOperator.apply(x, y);
      Scalar err = Vector2Norm.between(res, z);
      assertTrue(Scalars.lessThan(err, cmp));
      cmp = err;
    }
    Chop._08.requireZero(cmp);
  }

  public void testConvergenceSe2() {
    Tensor x = Tensors.vector(0.1, 0.2, 0.05);
    Tensor y = Tensors.vector(0.02, -0.1, -0.04);
    Tensor mX = Se2CoveringExponential.INSTANCE.exp(x);
    Tensor mY = Se2CoveringExponential.INSTANCE.exp(y);
    Tensor res = Se2CoveringExponential.INSTANCE.log(Se2CoveringGroup.INSTANCE.element(mX).combine(mY));
    Tensor ad = N.DOUBLE.of(LieAlgebras.se2());
    Scalar cmp = RealScalar.ONE;
    for (int degree = 1; degree < 6; ++degree) {
      BinaryOperator<Tensor> binaryOperator = BakerCampbellHausdorff.of(ad, degree);
      Tensor z = binaryOperator.apply(x, y);
      Scalar err = Vector2Norm.between(res, z);
      assertTrue(Scalars.lessThan(err, cmp));
      cmp = err;
    }
    Chop._08.requireZero(cmp);
  }

  public void testHe1() {
    _check(LieAlgebras.he1());
  }

  public void testSl2() {
    _check(LieAlgebras.sl2());
  }

  public void testSe2() {
    _check(LieAlgebras.se2());
  }

  public void testSo3() {
    _check(LieAlgebras.so3());
  }

  public void testJacobiFail() {
    Tensor ad = LieAlgebras.sl2();
    ad.set(Scalar::zero, Tensor.ALL, 1, 2);
    AssertFail.of(() -> BakerCampbellHausdorff.of(ad, 2));
  }

  public void testDegreeFail() {
    Tensor ad = Array.zeros(2, 2, 2);
    BakerCampbellHausdorff.of(ad, 1);
    AssertFail.of(() -> BakerCampbellHausdorff.of(ad, 0));
  }
}

// code by jph
package ch.alpine.sophus.lie.ad;

import java.io.IOException;
import java.util.Random;
import java.util.function.BinaryOperator;

import ch.alpine.sophus.math.SeriesInterface;
import ch.alpine.sophus.usr.AssertFail;
import ch.alpine.tensor.ExactTensorQ;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Array;
import ch.alpine.tensor.alg.UnitVector;
import ch.alpine.tensor.ext.Serialization;
import ch.alpine.tensor.ext.Timing;
import ch.alpine.tensor.mat.re.Det;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.d.DiscreteUniformDistribution;
import ch.alpine.tensor.sca.Chop;
import ch.alpine.tensor.spa.SparseArray;
import junit.framework.TestCase;

public class BakerCampbellHausdorffTest extends TestCase {
  private static void _check(Tensor ad, int degree) {
    BakerCampbellHausdorff bakerCampbellHausdorff = //
        new BakerCampbellHausdorff(ad, degree, Chop._14);
    SeriesInterface appx = (SeriesInterface) BakerCampbellHausdorff.of(ad, degree);
    int n = ad.length();
    Timing t_bch = Timing.stopped();
    Timing t_apx = Timing.stopped();
    for (int c0 = 0; c0 < n; ++c0)
      for (int c1 = 0; c1 < n; ++c1)
      // Distribution distribution2 = DiscreteUniformDistribution.of(-10, 10);
      // RandomVariate
      {
        Tensor x =
            // RandomVariate.of(distribution2, n);
            UnitVector.of(n, c0);
        Tensor y =
            // RandomVariate.of(distribution2, n);
            UnitVector.of(n, c1);
        {
          t_bch.start();
          Tensor res1 = bakerCampbellHausdorff.apply(x, y);
          t_bch.stop();
          t_apx.start();
          Tensor res2 = appx.apply(x, y);
          t_apx.stop();
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
    {
      Distribution distribution = DiscreteUniformDistribution.of(-10, 10);
      Tensor x = RandomVariate.of(distribution, n);
      Tensor y = RandomVariate.of(distribution, n);
      {
        t_bch.start();
        Tensor res1 = bakerCampbellHausdorff.apply(x, y);
        t_bch.stop();
        t_apx.start();
        Tensor res2 = appx.apply(x, y);
        t_apx.stop();
        assertEquals(res1, res2);
        ExactTensorQ.require(res1);
        Tensor res3 = bakerCampbellHausdorff.apply(y.negate(), x.negate()).negate();
        assertEquals(res1, res3);
      }
    }
    // System.out.println(String.format("bch: %10d", t_bch.nanoSeconds()));
    // System.out.println(String.format("apx: %10d", t_apx.nanoSeconds()));
  }

  private static final int[] DEGREES = new int[] { 6, 8 };

  public void testHe1() {
    for (int d : DEGREES)
      _check(TestHelper.he1(), d);
  }

  public void testSl2() {
    for (int d : DEGREES)
      _check(TestHelper.sl2(), d);
  }

  public void testSl2Sophus() {
    Tensor ad = Tensors.fromString( //
        "{{{0, 0, 0}, {0, 0, 1}, {0, -1, 0}}, {{0, 0, 1}, {0, 0, 0}, {-1, 0, 0}}, {{0, -1, 0}, {1, 0, 0}, {0, 0, 0}}}");
    for (int d : DEGREES)
      _check(ad, d);
    assertEquals(Det.of(KillingForm.of(ad)), RealScalar.of(-8));
  }

  public void testSe2() {
    for (int d : new int[] { 6, 8, 10 })
      _check(TestHelper.se2(), d);
  }

  public void testSo3() {
    for (int d : DEGREES)
      _check(TestHelper.so3(), d);
  }

  public void testOptimized() {
    Tensor ad = TestHelper.sl2();
    assertTrue(BakerCampbellHausdorff.of(ad, 6) instanceof BchSeries6);
    assertTrue(BakerCampbellHausdorff.of(ad, 8) instanceof BchSeries8);
    assertTrue(BakerCampbellHausdorff.of(ad, 10) instanceof BchSeriesA);
    assertTrue(BakerCampbellHausdorff.of(ad, 6, Chop._02) instanceof BchSeries6);
    assertTrue(BakerCampbellHausdorff.of(ad, 8, Chop._02) instanceof BchSeries8);
    assertTrue(BakerCampbellHausdorff.of(ad, 10, Chop._02) instanceof BchSeriesA);
  }

  public void testSparse() throws ClassNotFoundException, IOException {
    CliffordAlgebra cliffordAlgebra = CliffordAlgebra.of(1, 2);
    Tensor cp = cliffordAlgebra.cp();
    assertTrue(cp instanceof SparseArray);
    BinaryOperator<Tensor> binaryOperator = Serialization.copy(BakerCampbellHausdorff.of(cp, 3));
    int n = cp.length();
    Distribution distribution = DiscreteUniformDistribution.of(-10, 10);
    Random random = new Random(1234);
    Tensor x = RandomVariate.of(distribution, random, n).divide(RealScalar.of(20));
    Tensor y = RandomVariate.of(distribution, random, n).divide(RealScalar.of(20));
    Tensor apply = binaryOperator.apply(x, y);
    ExactTensorQ.require(apply);
  }

  public void testJacobiFail() throws ClassNotFoundException, IOException {
    Tensor ad = TestHelper.sl2();
    Serialization.copy(BakerCampbellHausdorff.of(ad, 2));
    ad.set(Scalar::zero, Tensor.ALL, 1, 2);
    AssertFail.of(() -> BakerCampbellHausdorff.of(ad, 2));
  }

  public void testDegreeFail() {
    Tensor ad = Array.sparse(2, 2, 2);
    BakerCampbellHausdorff.of(ad, 1);
    AssertFail.of(() -> BakerCampbellHausdorff.of(ad, 1, null));
    AssertFail.of(() -> BakerCampbellHausdorff.of(ad, 0));
  }

  public void testChopNullFail() {
    Tensor ad = TestHelper.sl2();
    AssertFail.of(() -> BakerCampbellHausdorff.of(ad, 6, null));
  }

  public void testMatrixLogExpExpSe2() {
    MatrixAlgebra matrixAlgebra = new MatrixAlgebra(TestHelper.se2_basis());
    TestHelper.check(matrixAlgebra, 8);
  }

  public void testMatrixLogExpExpSo3() {
    MatrixAlgebra matrixAlgebra = new MatrixAlgebra(TestHelper.so3_basis());
    TestHelper.check(matrixAlgebra, 8);
  }
}

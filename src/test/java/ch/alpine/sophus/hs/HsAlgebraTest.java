// code by jph
package ch.alpine.sophus.hs;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.random.RandomGenerator;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import ch.alpine.sophus.hs.HsAlgebra.Decomp;
import ch.alpine.sophus.hs.s.SnExponential;
import ch.alpine.sophus.lie.LieAlgebraAds;
import ch.alpine.sophus.lie.MatrixAlgebra;
import ch.alpine.sophus.lie.he.HeAlgebra;
import ch.alpine.sophus.lie.se2.Se2CoveringGroup;
import ch.alpine.sophus.lie.se2.Se2Matrix;
import ch.alpine.sophus.lie.sl.Sl2Algebra;
import ch.alpine.sophus.lie.so.Rodrigues;
import ch.alpine.sophus.lie.so.So3Group;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Array;
import ch.alpine.tensor.alg.Join;
import ch.alpine.tensor.alg.UnitVector;
import ch.alpine.tensor.api.TensorBinaryOperator;
import ch.alpine.tensor.lie.bch.BakerCampbellHausdorff;
import ch.alpine.tensor.lie.rot.RotationMatrix;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.mat.ex.MatrixExp;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.pdf.d.DiscreteUniformDistribution;
import ch.alpine.tensor.sca.Chop;

class HsAlgebraTest {
  @Test
  void testSe2() {
    MatrixAlgebra matrixAlgebra = new MatrixAlgebra(Se2CoveringGroup.INSTANCE.matrixBasis());
    Tensor ad = matrixAlgebra.ad();
    TensorBinaryOperator bch = BakerCampbellHausdorff.of(ad, 10, Tolerance.CHOP);
    bch.apply(Tensors.vector(.1, .1, .1), Tensors.vector(.1, .2, .3));
    HsAlgebra hsAlgebra = new HsAlgebra(ad, 2, 6);
    assertTrue(hsAlgebra.isReductive());
    assertTrue(hsAlgebra.isSymmetric());
  }

  @Test
  void testSe2Actions() {
    MatrixAlgebra matrixAlgebra = new MatrixAlgebra(Se2CoveringGroup.INSTANCE.matrixBasis());
    Tensor ad = matrixAlgebra.ad();
    TensorBinaryOperator bch = BakerCampbellHausdorff.of(ad, 10, Tolerance.CHOP);
    bch.apply(Tensors.vector(.1, .1, .1), Tensors.vector(.1, .2, .3));
    HsAlgebra hsAlgebra = new HsAlgebra(ad, 2, 8);
    {
      Tensor g = Tensors.vector(1, 0, 0);
      Tensor p = Tensors.vector(2, 4);
      Tensor q = hsAlgebra.action(g, p);
      assertEquals(q, Tensors.vector(3, 4));
    }
    {
      Tensor g = Tensors.vector(0, 1, 0);
      Tensor p = Tensors.vector(2, 4);
      Tensor q = hsAlgebra.action(g, p);
      assertEquals(q, Tensors.vector(2, 5));
    }
    {
      double angle = Math.PI / 16;
      Tensor g = Tensors.vector(0, 0, angle);
      Tensor p = Tensors.vector(.05, .02);
      Tensor q1 = hsAlgebra.action(g, p);
      Tensor q2 = RotationMatrix.of(angle).dot(p);
      Tolerance.CHOP.requireClose(q1, q2);
    }
  }

  @Test
  void testSe2ActionsExp() {
    Tensor basis = Se2CoveringGroup.INSTANCE.matrixBasis();
    MatrixAlgebra matrixAlgebra = new MatrixAlgebra(basis);
    Tensor ad = matrixAlgebra.ad();
    TensorBinaryOperator bch = BakerCampbellHausdorff.of(ad, 10, Tolerance.CHOP);
    bch.apply(Tensors.vector(.1, .1, .1), Tensors.vector(.1, .2, .3));
    HsAlgebra hsAlgebra = new HsAlgebra(ad, 2, 8);
    RandomGenerator randomGenerator = ThreadLocalRandom.current();
    Distribution distribution = UniformDistribution.of(-0.05, 0.05);
    Tensor g = RandomVariate.of(distribution, randomGenerator, 3);
    Tensor p = RandomVariate.of(distribution, randomGenerator, 2);
    Tensor q1 = hsAlgebra.action(g, p);
    Tensor xyz = Se2CoveringGroup.INSTANCE.exponential0().exp(g);
    Tensor mat = Se2Matrix.of(xyz);
    Tensor q2 = mat.dot(p.copy().append(RealScalar.ONE)).extract(0, 2);
    Tolerance.CHOP.requireClose(q1, q2);
    Tensor exp = MatrixExp.of(g.dot(basis));
    Tensor q3 = exp.dot(p.copy().append(RealScalar.ONE)).extract(0, 2);
    Tolerance.CHOP.requireClose(q1, q3);
  }

  @Test
  void testSe2Fail() {
    Tensor basis = Se2CoveringGroup.INSTANCE.matrixBasis();
    MatrixAlgebra matrixAlgebra = new MatrixAlgebra(basis);
    Tensor ad = matrixAlgebra.ad();
    TensorBinaryOperator bch = BakerCampbellHausdorff.of(ad, 10, Tolerance.CHOP);
    bch.apply(Tensors.vector(.1, .1, .1), Tensors.vector(.1, .2, .3));
    assertThrows(Exception.class, () -> new HsAlgebra(ad, 1, 6));
  }

  @Test
  void testSo3() {
    MatrixAlgebra matrixAlgebra = new MatrixAlgebra(So3Group.INSTANCE.matrixBasis());
    HsAlgebra hsAlgebra = new HsAlgebra(matrixAlgebra.ad(), 2, 6);
    assertTrue(hsAlgebra.isReductive());
    assertTrue(hsAlgebra.isSymmetric());
    Distribution distribution = UniformDistribution.of(-0.1, 0.1);
    hsAlgebra.action(RandomVariate.of(distribution, 3), RandomVariate.of(distribution, 2));
    hsAlgebra.projection(RandomVariate.of(distribution, 3));
  }

  private static Tensor sl2_basis_mh() {
    Tensor m0 = Tensors.fromString("{{0, 1}, {-1, 0}}");
    Tensor m1 = Tensors.fromString("{{0, 1}, {1, 0}}");
    Tensor h0 = Tensors.fromString("{{1, 0}, {0, -1}}");
    return Tensors.of(m0, m1, h0);
  }

  @Test
  void testSl2() {
    MatrixAlgebra matrixAlgebra = new MatrixAlgebra(sl2_basis_mh());
    HsAlgebra hsAlgebra = new HsAlgebra(matrixAlgebra.ad(), 2, 6);
    assertEquals(hsAlgebra.dimG(), 3);
    assertEquals(hsAlgebra.dimM(), 2);
    assertEquals(hsAlgebra.dimH(), 1);
    assertTrue(hsAlgebra.isReductive());
    assertTrue(hsAlgebra.isSymmetric());
    Distribution distribution = UniformDistribution.of(-0.1, 0.1);
    hsAlgebra.action(RandomVariate.of(distribution, 3), RandomVariate.of(distribution, 2));
    // hsAlgebra.printTable();
  }

  @Test
  void testSo3Simple() {
    Tensor g = Tensors.vector(0.0, 0.0, Math.PI / 2);
    Tensor m = Tensors.vector(0.1, 0.0);
    MatrixAlgebra matrixAlgebra = new MatrixAlgebra(So3Group.INSTANCE.matrixBasis());
    HsAlgebra hsAlgebra = new HsAlgebra(matrixAlgebra.ad(), 2, 6);
    Tensor res = hsAlgebra.action(g, m);
    Chop._03.requireClose(res, Tensors.vector(0.0, 0.1));
    Tensor p = UnitVector.of(3, 2);
    Tensor rotation = Rodrigues.vectorExp(g);
    SnExponential snExponential = new SnExponential(p);
    Tensor v = m.copy().append(RealScalar.ZERO);
    Tensor snm = snExponential.exp(v);
    Tensor dot = rotation.dot(snm);
    Tensor bak = snExponential.log(dot);
    Tolerance.CHOP.requireClose(bak, Tensors.vector(0, 0.1, 0));
  }

  @Test
  void testSo3ZeroMap() {
    Tensor g = Tensors.vector(0.1, 0.2, 0);
    Tensor m = Tensors.vector(0.0, 0.0);
    MatrixAlgebra matrixAlgebra = new MatrixAlgebra(So3Group.INSTANCE.matrixBasis());
    HsAlgebra hsAlgebra = new HsAlgebra(matrixAlgebra.ad(), 2, 6);
    Tensor res = hsAlgebra.action(g, m);
    // System.out.println(res);
    Chop._03.requireClose(res, Tensors.vector(0.1, 0.2));
    Tensor p = UnitVector.of(3, 2);
    Tensor rotation = Rodrigues.vectorExp(g);
    SnExponential snExponential = new SnExponential(p);
    Tensor v = m.copy().append(RealScalar.ZERO);
    Tensor snm = snExponential.exp(v);
    Tensor dot = rotation.dot(snm);
    Tensor bak = snExponential.log(dot);
    bak.map(Scalar::zero);
  }

  private static final HsAlgebra[] HS_ALGEBRAS = { //
      new HsAlgebra(LieAlgebraAds.se(2), 2, 6), //
      new HsAlgebra(new MatrixAlgebra(So3Group.INSTANCE.matrixBasis()).ad(), 2, 6), //
      new HsAlgebra(new HeAlgebra(1).ad(), 2, 6), //
      new HsAlgebra(new HeAlgebra(2).ad(), 3, 6), //
      new HsAlgebra(Sl2Algebra.INSTANCE.ad(), 2, 6), //
      new HsAlgebra(LieAlgebraAds.sl(3), 6, 6) };

  static List<HsAlgebra> testSeveral() {
    return Arrays.asList(HS_ALGEBRAS);
  }

  @ParameterizedTest
  @MethodSource("testSeveral")
  void testHsAlgebra(HsAlgebra hsAlgebra) {
    Distribution distribution = UniformDistribution.of(-0.05, 0.05);
    RandomGenerator randomGenerator = new Random(3);
    {
      TensorBinaryOperator bch = BakerCampbellHausdorff.of(hsAlgebra.ad(), 6);
      Scalar lambda = RandomVariate.of(distribution);
      Tensor ga = RandomVariate.of(distribution, randomGenerator, hsAlgebra.dimG());
      Tensor m1 = RandomVariate.of(distribution, randomGenerator, hsAlgebra.dimM());
      Tensor m2 = hsAlgebra.action(ga, m1);
      Tensor delta = bch.apply(hsAlgebra.lift(m1).negate(), hsAlgebra.lift(m2)).multiply(lambda);
      Tensor mi = hsAlgebra.projection(bch.apply(hsAlgebra.lift(m1), delta));
      Tensor mj = hsAlgebra.projection(bch.apply(hsAlgebra.lift(m1), hsAlgebra.lift(hsAlgebra.projection(delta))));
      Tensor mk = hsAlgebra.action(hsAlgebra.lift(m1), hsAlgebra.projection(delta));
      Tolerance.CHOP.requireClose(mi, mj);
      Tolerance.CHOP.requireClose(mk, mj);
    }
    {
      TensorBinaryOperator bch = BakerCampbellHausdorff.of(hsAlgebra.ad(), 6);
      Tensor g = RandomVariate.of(distribution, randomGenerator, hsAlgebra.dimG());
      Tensor h = Join.of(Array.zeros(hsAlgebra.dimM()), RandomVariate.of(distribution, randomGenerator, hsAlgebra.dimH()));
      Tensor p1 = hsAlgebra.projection(g);
      Tensor p2 = hsAlgebra.projection(bch.apply(g, h));
      Chop._11.requireClose(p1, p2);
    }
    {
      TensorBinaryOperator bch = BakerCampbellHausdorff.of(hsAlgebra.ad(), 6);
      Tensor g = RandomVariate.of(distribution, randomGenerator, hsAlgebra.dimG());
      Decomp decomp1 = hsAlgebra.new Decomp(HsPair.seed(g));
      Tolerance.CHOP.requireClose(bch.apply(g, decomp1.h), hsAlgebra.lift(decomp1.m));
      Tolerance.CHOP.requireClose(g, bch.apply(hsAlgebra.lift(decomp1.m), decomp1.h.negate()));
      Decomp decomp2 = hsAlgebra.new Decomp(new HsPair(bch.apply(g, decomp1.h), decomp1.h));
      Tolerance.CHOP.requireClose(bch.apply(g, decomp2.h), hsAlgebra.lift(decomp2.m));
      Tolerance.CHOP.requireClose(g, bch.apply(hsAlgebra.lift(decomp2.m), decomp2.h.negate()));
    }
    {
      Tensor m1 = RandomVariate.of(distribution, randomGenerator, hsAlgebra.dimM());
      Tensor m2 = hsAlgebra.projection(hsAlgebra.lift(m1));
      Tolerance.CHOP.requireClose(m1, m2);
    }
    {
      TensorBinaryOperator bch = BakerCampbellHausdorff.of(hsAlgebra.ad(), 10);
      Tensor m = RandomVariate.of(distribution, randomGenerator, hsAlgebra.dimM());
      Tensor ml = hsAlgebra.lift(m);
      Tensor h = Join.of(m.map(Scalar::zero), RandomVariate.of(distribution, randomGenerator, hsAlgebra.dimH()));
      Tensor g = bch.apply(ml, h.negate()); // this is the equation
      Decomp decomp = hsAlgebra.new Decomp(HsPair.seed(g));
      Chop._10.requireClose(m, decomp.m);
      Chop._10.requireClose(h, decomp.h);
    }
  }

  @Test
  void testHTrivial() {
    Distribution distribution = DiscreteUniformDistribution.of(-100, 100);
    RandomGenerator randomGenerator = new Random(1);
    for (HsAlgebra hsAlgebra : HS_ALGEBRAS) {
      if (hsAlgebra.isHTrivial()) {
        // System.out.println("HERE");
        // System.out.println(hsAlgebra.ad());
        Tensor g = RandomVariate.of(distribution, randomGenerator, hsAlgebra.dimG()).divide(RealScalar.of(200));
        Tensor ghinv = g.negate();
        IntStream.range(0, hsAlgebra.dimM()).forEach(i -> ghinv.set(Scalar::zero, i));
        TensorBinaryOperator bch = BakerCampbellHausdorff.of(hsAlgebra.ad(), 8);
        Tensor prj = bch.apply(g, ghinv);
        Tensor rem = prj.extract(hsAlgebra.dimM(), hsAlgebra.dimG());
        assertEquals(rem, Array.zeros(rem.length()));
      }
    }
  }

  @Test
  void testLieAlgebra() {
    Tensor basis = Se2CoveringGroup.INSTANCE.matrixBasis();
    MatrixAlgebra matrixAlgebra = new MatrixAlgebra(basis);
    Tensor se2_ad = matrixAlgebra.ad();
    TensorBinaryOperator bch = BakerCampbellHausdorff.of(se2_ad, 10, Tolerance.CHOP);
    bch.apply(Tensors.vector(.1, .1, .1), Tensors.vector(.1, .2, .3));
    for (Tensor ad : new Tensor[] { //
        se2_ad, new HeAlgebra(2).ad(), LieAlgebraAds.sl(3) }) {
      HsAlgebra hsAlgebra = new HsAlgebra(ad, ad.length(), 6);
      assertFalse(hsAlgebra.isSymmetric());
      assertTrue(hsAlgebra.isReductive());
    }
  }
}

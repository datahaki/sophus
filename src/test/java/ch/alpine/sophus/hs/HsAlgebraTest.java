// code by jph
package ch.alpine.sophus.hs;

import java.util.Random;
import java.util.function.BinaryOperator;

import ch.alpine.sophus.hs.HsAlgebra.Decomp;
import ch.alpine.sophus.hs.sn.SnExponential;
import ch.alpine.sophus.lie.he.HeAlgebra;
import ch.alpine.sophus.lie.se2.Se2Algebra;
import ch.alpine.sophus.lie.se2.Se2Matrix;
import ch.alpine.sophus.lie.se2c.Se2CoveringExponential;
import ch.alpine.sophus.lie.se3.Se3Algebra;
import ch.alpine.sophus.lie.sl2.Sl2Algebra;
import ch.alpine.sophus.lie.so3.Rodrigues;
import ch.alpine.sophus.lie.so3.So3Algebra;
import ch.alpine.sophus.usr.AssertFail;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Array;
import ch.alpine.tensor.alg.Join;
import ch.alpine.tensor.alg.UnitVector;
import ch.alpine.tensor.lie.ad.MatrixAlgebra;
import ch.alpine.tensor.lie.r2.RotationMatrix;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.mat.ex.MatrixExp;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.UniformDistribution;
import ch.alpine.tensor.sca.Chop;
import junit.framework.TestCase;

public class HsAlgebraTest extends TestCase {
  public void testSe2() {
    HsAlgebra hsAlgebra = new HsAlgebra(Se2Algebra.INSTANCE.ad(), 2, 6);
    assertTrue(hsAlgebra.isReductive());
    assertTrue(hsAlgebra.isSymmetric());
  }

  public void testSe2Actions() {
    HsAlgebra hsAlgebra = new HsAlgebra(Se2Algebra.INSTANCE.ad(), 2, 8);
    {
      Tensor g = Tensors.vector(1, 0, 0);
      Tensor p = Tensors.vector(2, 4);
      Tensor q = hsAlgebra.action(g, p);
      // ExactTensorQ.require(q);
      assertEquals(q, Tensors.vector(3, 4));
    }
    {
      Tensor g = Tensors.vector(0, 1, 0);
      Tensor p = Tensors.vector(2, 4);
      Tensor q = hsAlgebra.action(g, p);
      // ExactTensorQ.require(q);
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

  public void testSe2ActionsExp() {
    HsAlgebra hsAlgebra = new HsAlgebra(Se2Algebra.INSTANCE.ad(), 2, 8);
    Random random = new Random();
    Distribution distribution = UniformDistribution.of(-0.05, 0.05);
    Tensor g = RandomVariate.of(distribution, random, 3);
    Tensor p = RandomVariate.of(distribution, random, 2);
    Tensor q1 = hsAlgebra.action(g, p);
    Tensor xyz = Se2CoveringExponential.INSTANCE.exp(g);
    Tensor mat = Se2Matrix.of(xyz);
    Tensor q2 = mat.dot(p.copy().append(RealScalar.ONE)).extract(0, 2);
    Tolerance.CHOP.requireClose(q1, q2);
    Tensor exp = MatrixExp.of(g.dot(Se2Algebra.INSTANCE.basis()));
    Tensor q3 = exp.dot(p.copy().append(RealScalar.ONE)).extract(0, 2);
    Tolerance.CHOP.requireClose(q1, q3);
  }

  public void testSe2Fail() {
    AssertFail.of(() -> new HsAlgebra(Se2Algebra.INSTANCE.ad(), 1, 6));
  }

  public void testSo3() {
    HsAlgebra hsAlgebra = new HsAlgebra(So3Algebra.INSTANCE.ad(), 2, 6);
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

  public void testSl2() {
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

  public void testSo3Simple() {
    Tensor g = Tensors.vector(0.0, 0.0, Math.PI / 2);
    Tensor m = Tensors.vector(0.1, 0.0);
    HsAlgebra hsAlgebra = new HsAlgebra(So3Algebra.INSTANCE.ad(), 2, 6);
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

  public void testSo3ZeroMap() {
    Tensor g = Tensors.vector(0.1, 0.2, 0);
    Tensor m = Tensors.vector(0.0, 0.0);
    HsAlgebra hsAlgebra = new HsAlgebra(So3Algebra.INSTANCE.ad(), 2, 6);
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
      new HsAlgebra(Se2Algebra.INSTANCE.ad(), 2, 6), //
      new HsAlgebra(Se3Algebra.INSTANCE.ad(), 3, 6), //
      new HsAlgebra(So3Algebra.INSTANCE.ad(), 2, 6), //
      new HsAlgebra(new HeAlgebra(1).ad(), 2, 6), //
      new HsAlgebra(new HeAlgebra(2).ad(), 3, 6), //
      new HsAlgebra(Sl2Algebra.INSTANCE.ad(), 2, 6) };

  public void testAction() {
    Distribution distribution = UniformDistribution.of(-0.05, 0.05);
    Random random = new Random();
    for (HsAlgebra hsAlgebra : HS_ALGEBRAS) {
      BinaryOperator<Tensor> bch = hsAlgebra.lieAlgebra().bch(6);
      Scalar lambda = RandomVariate.of(distribution);
      Tensor ga = RandomVariate.of(distribution, random, hsAlgebra.dimG());
      Tensor m1 = RandomVariate.of(distribution, random, hsAlgebra.dimM());
      Tensor m2 = hsAlgebra.action(ga, m1);
      Tensor delta = bch.apply(hsAlgebra.lift(m1).negate(), hsAlgebra.lift(m2)).multiply(lambda);
      Tensor mi = hsAlgebra.projection(bch.apply(hsAlgebra.lift(m1), delta));
      Tensor mj = hsAlgebra.projection(bch.apply(hsAlgebra.lift(m1), hsAlgebra.lift(hsAlgebra.projection(delta))));
      Tensor mk = hsAlgebra.action(hsAlgebra.lift(m1), hsAlgebra.projection(delta));
      Tolerance.CHOP.requireClose(mi, mj);
      Tolerance.CHOP.requireClose(mk, mj);
    }
  }

  public void testProj() {
    Distribution distribution = UniformDistribution.of(-0.05, 0.05);
    Random random = new Random(1);
    for (HsAlgebra hsAlgebra : HS_ALGEBRAS) {
      BinaryOperator<Tensor> bch = hsAlgebra.lieAlgebra().bch(6);
      Tensor g = RandomVariate.of(distribution, random, hsAlgebra.dimG());
      Tensor h = Join.of(Array.zeros(hsAlgebra.dimM()), RandomVariate.of(distribution, random, hsAlgebra.dimH()));
      Tensor p1 = hsAlgebra.projection(g);
      Tensor p2 = hsAlgebra.projection(bch.apply(g, h));
      Chop._11.requireClose(p1, p2);
    }
  }

  public void testDecomp() {
    Distribution distribution = UniformDistribution.of(-0.05, 0.05);
    Random random = new Random(2);
    for (HsAlgebra hsAlgebra : HS_ALGEBRAS) {
      BinaryOperator<Tensor> bch = hsAlgebra.lieAlgebra().bch(6);
      Tensor g = RandomVariate.of(distribution, random, hsAlgebra.dimG());
      Decomp decomp1 = hsAlgebra.new Decomp(HsPair.seed(g));
      Tolerance.CHOP.requireClose(bch.apply(g, decomp1.h), hsAlgebra.lift(decomp1.m));
      Tolerance.CHOP.requireClose(g, bch.apply(hsAlgebra.lift(decomp1.m), decomp1.h.negate()));
      Decomp decomp2 = hsAlgebra.new Decomp(new HsPair(bch.apply(g, decomp1.h), decomp1.h));
      Tolerance.CHOP.requireClose(bch.apply(g, decomp2.h), hsAlgebra.lift(decomp2.m));
      Tolerance.CHOP.requireClose(g, bch.apply(hsAlgebra.lift(decomp2.m), decomp2.h.negate()));
    }
  }

  public void testLiftProjection() {
    Distribution distribution = UniformDistribution.of(-0.05, 0.05);
    Random random = new Random();
    for (HsAlgebra hsAlgebra : HS_ALGEBRAS) {
      Tensor m1 = RandomVariate.of(distribution, random, hsAlgebra.dimM());
      Tensor m2 = hsAlgebra.projection(hsAlgebra.lift(m1));
      Tolerance.CHOP.requireClose(m1, m2);
    }
  }

  public void testDecompCheat() {
    Distribution distribution = UniformDistribution.of(-0.05, 0.05);
    Random random = new Random();
    for (HsAlgebra hsAlgebra : HS_ALGEBRAS) {
      BinaryOperator<Tensor> bch = hsAlgebra.lieAlgebra().bch(6);
      Tensor m = RandomVariate.of(distribution, random, hsAlgebra.dimM());
      Tensor ml = hsAlgebra.lift(m);
      Tensor h = Join.of(m.map(Scalar::zero), RandomVariate.of(distribution, random, hsAlgebra.dimH()));
      Tensor g = bch.apply(ml, h.negate()); // this is the equation
      Decomp decomp = hsAlgebra.new Decomp(HsPair.seed(g));
      Tolerance.CHOP.requireClose(m, decomp.m);
      Tolerance.CHOP.requireClose(h, decomp.h);
    }
  }
}

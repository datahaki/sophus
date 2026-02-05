// code by jph
package ch.alpine.sophus.lie.se2;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Random;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.lie.LieAlgebraAds;
import ch.alpine.sophus.lie.LieGroup;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Scalars;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Array;
import ch.alpine.tensor.api.TensorBinaryOperator;
import ch.alpine.tensor.chq.ExactTensorQ;
import ch.alpine.tensor.lie.bch.BakerCampbellHausdorff;
import ch.alpine.tensor.mat.IdentityMatrix;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.mat.re.Inverse;
import ch.alpine.tensor.nrm.Vector2Norm;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomSample;
import ch.alpine.tensor.pdf.RandomSampleInterface;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.sca.Chop;
import ch.alpine.tensor.sca.Clips;
import ch.alpine.tensor.sca.Sign;
import showcase.GroupCheck;

class Se2CoveringGroupTest {
  private static final RandomSampleInterface RANDOM_SAMPLE_INTERFACE = //
      Se2CoveringRandomSample.uniform(UniformDistribution.of(Clips.absolute(10)));

  @Test
  void testSimple() {
    Tensor p = Tensors.vector(1, 2, 3);
    // Se2CoveringGroupElement se2CoveringGroupElement = Se2CoveringGroup.INSTANCE.element(p);
    Tensor tensor = Se2CoveringGroup.INSTANCE.combine(p, Tensors.vector(0, 0, -3));
    assertEquals(tensor, Tensors.vector(1, 2, 0));
    GroupCheck.check(Se2CoveringGroup.INSTANCE);
  }

  @Test
  void testConvergenceSe2() {
    Tensor x = Tensors.vector(0.1, 0.2, 0.05);
    Tensor y = Tensors.vector(0.02, -0.1, -0.04);
    Tensor mX = Se2CoveringGroup.INSTANCE.exponential0().exp(x);
    Tensor mY = Se2CoveringGroup.INSTANCE.exponential0().exp(y);
    Tensor res = Se2CoveringGroup.INSTANCE.exponential0().log(Se2CoveringGroup.INSTANCE.combine(mX, mY));
    Scalar cmp = RealScalar.ONE;
    for (int degree = 1; degree < 6; ++degree) {
      TensorBinaryOperator binaryOperator = BakerCampbellHausdorff.of(LieAlgebraAds.se(2), degree);
      Tensor z = binaryOperator.apply(x, y);
      Scalar err = Vector2Norm.between(res, z);
      assertTrue(Scalars.lessThan(err, cmp));
      cmp = err;
    }
    Chop._08.requireZero(cmp);
  }

  @Test
  void testArticle() {
    Tensor tensor = Se2CoveringGroup.INSTANCE.split( //
        Tensors.vector(1, 2, 3), Tensors.vector(4, 5, 6), RealScalar.of(0.7));
    Chop._14.requireClose(tensor, Tensors.fromString("{4.483830852817113, 3.2143505344919467, 5.1}"));
  }

  @Test
  void testCenter() {
    // mathematica gives: {2.26033, -0.00147288, 0.981748}
    Tensor p = Tensors.vector(2.017191762967754, -0.08474511292102775, 0.9817477042468103);
    Tensor q = Tensors.vector(2.503476971090440, +0.08179934782700435, 0.9817477042468102);
    Scalar scalar = RealScalar.of(0.5);
    Tensor delta = Se2CoveringGroup.INSTANCE.diffOp(p).apply(q);
    Tensor x = Se2CoveringGroup.INSTANCE.exponential0().log(delta).multiply(scalar);
    x.get();
    // x= {0.20432112230000457, -0.1559021143001622, -5.551115123125783E-17}
    // mathematica gives
    // x= {0.204321, .......... -0.155902, ......... -5.55112*10^-17}
    // Tensor exp_x = Se2CoveringIntegrator.INSTANCE.spin(Tensors.vector(0, 0, 0), x);
    // exp_x == {0.20432112230000457, -0.1559021143001622, -5.551115123125783E-17}
    Tensor tensor = Se2CoveringGroup.INSTANCE.split(p, q, scalar);
    // {2.260334367029097, -0.0014728825470118057, 0.9817477042468103}
    Chop._14.requireClose(tensor, //
        Tensors.fromString("{2.260334367029097, -0.0014728825470118057, 0.9817477042468103}"));
  }

  @Test
  void testBiinvariantMean() {
    Distribution distribution = UniformDistribution.of(-3, 8);
    Distribution wd = UniformDistribution.unit();
    int success = 0;
    for (int count = 0; count < 100; ++count) {
      Tensor p = RandomVariate.of(distribution, 3);
      Tensor q = RandomVariate.of(distribution, 3);
      Scalar w = RandomVariate.of(wd);
      Tensor mean = Se2CoveringGroup.INSTANCE.biinvariantMean().mean(Tensors.of(p, q), Tensors.of(RealScalar.ONE.subtract(w), w));
      Tensor splt = Se2CoveringGroup.INSTANCE.split(p, q, w);
      if (Chop._12.isClose(mean, splt))
        ++success;
    }
    assertTrue(90 < success);
  }

  private static final LieGroup LIE_GROUP = Se2CoveringGroup.INSTANCE;

  @Test
  void testSimpleXY() {
    Tensor x = Tensors.vector(3, 2, 0).unmodifiable();
    Tensor g = LIE_GROUP.exponential0().exp(x);
    ExactTensorQ.require(g);
    assertEquals(g, x);
    Tensor y = LIE_GROUP.exponential0().log(g);
    ExactTensorQ.require(y);
    assertEquals(y, x);
  }

  @Test
  void testAdjointExp() {
    // reference Pennec/Arsigny 2012 p.13
    // g.Exp[x] == Exp[Ad(g).x].g
    Distribution distribution = UniformDistribution.of(Clips.absolute(10));
    for (int count = 0; count < 10; ++count) {
      Tensor g = RandomVariate.of(distribution, 3); // element
      Tensor x = RandomVariate.of(distribution, 3); // vector
      Tensor lhs = LIE_GROUP.combine(g, LIE_GROUP.exponential0().exp(x)); // g.Exp[x]
      Tensor rhs = LIE_GROUP.combine(LIE_GROUP.exponential0().exp(LIE_GROUP.adjoint(g, x)), g); // Exp[Ad(g).x].g
      Chop._10.requireClose(lhs, rhs);
    }
  }

  @Test
  void testAdjointLog() {
    // reference Pennec/Arsigny 2012 p.13
    // Log[g.m.g^-1] == Ad(g).Log[m]
    Random random = new Random(3);
    Distribution distribution = UniformDistribution.of(Clips.absolute(10));
    for (int count = 0; count < 10; ++count) {
      Tensor g = RandomVariate.of(distribution, random, 3); // element
      Tensor m = RandomVariate.of(distribution, random, 3); // vector
      Tensor lhs = LIE_GROUP.exponential0().log( //
          LIE_GROUP.combine(LIE_GROUP.combine(g, m), LIE_GROUP.invert(g))); // Log[g.m.g^-1]
      Tensor rhs = LIE_GROUP.adjoint(g, LIE_GROUP.exponential0().log(m)); // Ad(g).Log[m]
      Chop._10.requireClose(lhs, rhs);
    }
  }

  @Test
  void testSimpleLinearSubspace() {
    for (int theta = -10; theta <= 10; ++theta) {
      Tensor x = Tensors.vector(0, 0, theta).unmodifiable();
      Tensor g = Se2CoveringGroup.INSTANCE.exponential0().exp(x);
      assertEquals(g, x);
      Tensor y = Se2CoveringGroup.INSTANCE.exponential0().log(g);
      assertEquals(y, x);
    }
  }

  @Test
  void testQuantity123() {
    Tensor xya = Tensors.fromString("{1[m], 2[m], 0.3}");
    Tensor log = LIE_GROUP.exponential0().log(xya);
    Chop._12.requireClose(log, Tensors.fromString("{1.2924887258384925[m], 1.834977451676985[m], 0.3}"));
    Tensor exp = LIE_GROUP.exponential0().exp(log);
    Chop._12.requireClose(exp, xya);
  }

  private static Tensor adjoint(LieGroup lieGroup, Tensor p) {
    return Tensor.of(IdentityMatrix.of(3).stream().map(X -> lieGroup.adjoint(p, X)));
  }

  @Test
  void testCirc() {
    Distribution distribution = NormalDistribution.standard();
    for (int index = 0; index < 10; ++index) {
      Tensor xya = RandomVariate.of(distribution, 3);
      Tensor other = RandomVariate.of(distribution, 3);
      Tensor result = Se2CoveringGroup.INSTANCE.combine(xya, other);
      Tensor prod = Se2Matrix.of(xya).dot(Se2Matrix.of(other));
      Tensor matrix = Se2Matrix.of(result);
      Chop._10.requireClose(prod, matrix);
    }
  }

  @Test
  void testInverse() {
    Distribution distribution = NormalDistribution.standard();
    for (int index = 0; index < 10; ++index) {
      Tensor xya = RandomVariate.of(distribution, 3);
      Tensor result = Se2CoveringGroup.INSTANCE.diffOp(xya).apply(Array.zeros(3));
      Tensor prod = Inverse.of(Se2Matrix.of(xya));
      Tensor matrix = Se2Matrix.of(result);
      Chop._10.requireClose(prod, matrix);
    }
  }

  @Test
  void testInverseCirc() {
    Distribution distribution = NormalDistribution.standard();
    for (int index = 0; index < 10; ++index) {
      Tensor xya = RandomVariate.of(distribution, 3);
      Tensor result = Se2CoveringGroup.INSTANCE.diffOp(xya).apply(Array.zeros(3));
      Tensor circ = Se2CoveringGroup.INSTANCE.combine(xya, result);
      Chop._14.requireAllZero(circ);
    }
  }

  @Test
  void testIntegrator() {
    Distribution distribution = NormalDistribution.of(0, 10);
    for (int index = 0; index < 10; ++index) {
      Tensor xya = RandomVariate.of(distribution, 3);
      Tensor v = RandomVariate.of(distribution, 3);
      Tensor other = Se2CoveringGroup.INSTANCE.exponential0().exp(v);
      Tensor result = Se2CoveringGroup.INSTANCE.combine(xya, other);
      Tensor prod = Se2CoveringGroup.INSTANCE.spin(xya, v);
      Chop._10.requireClose(prod, result);
    }
  }

  @Test
  void testQuantity() {
    Tensor xya = Tensors.fromString("{1[m], 2[m], 0.34}");
    Tensor oth = Tensors.fromString("{-.3[m], 0.8[m], -0.5}");
    Tensor inverse = Se2CoveringGroup.INSTANCE.diffOp(xya).apply(xya.map(Scalar::zero));
    assertEquals(inverse, Tensors.fromString("{-1.6097288498099749[m], -1.552022238915878[m], -0.34}"));
    Tensor circ = Se2CoveringGroup.INSTANCE.combine(xya, oth);
    assertEquals(circ, Tensors.fromString("{0.4503839266288446[m], 2.654157604780433[m], -0.15999999999999998}"));
  }

  @Test
  void testMatrixAction() {
    Distribution distribution = NormalDistribution.of(0, 10);
    for (int index = 0; index < 10; ++index) {
      Tensor xya1 = RandomVariate.of(distribution, 3);
      Tensor xya2 = RandomVariate.of(distribution, 3);
      Tensor xya3 = Se2CoveringGroup.INSTANCE.combine(xya1, xya2);
      Tensor xyam = Se2Matrix.of(xya1).dot(Se2Matrix.of(xya2));
      Chop._12.requireClose(Se2Matrix.of(xya3), xyam);
    }
  }

  @Test
  void testNoWrap() {
    Tensor p = Tensors.vector(1, 2, 3);
    // Se2CoveringGroupElement element = new Se2CoveringGroupElement();
    Tensor tensor = Se2CoveringGroup.INSTANCE.combine(p, Tensors.vector(6, 7, 8));
    assertTrue(Sign.isPositive(tensor.Get(2)));
  }

  @Test
  void testInverseTensor() {
    Tensor xya = Tensors.fromString("{1[m], 2[m], 0.34}");
    // Se2CoveringGroupElement element = new Se2CoveringGroupElement(xya);
    Tensor coordin = Se2CoveringGroup.INSTANCE.invert(xya);
    Tensor combine = Se2CoveringGroup.INSTANCE.combine(xya, coordin);
    Chop._12.requireClose(combine, Tensors.fromString("{0[m], 0[m], 0}"));
    Se2CoveringGroup.INSTANCE.dL(xya, Tensors.fromString("{2[m*s^-1], 3[m*s^-1], 4[s^-1]}"));
    Tensor dR = Se2CoveringGroup.INSTANCE.dR(xya, Tensors.fromString("{2[m*s^-1], 3[m*s^-1], 4[s^-1]}"));
    ExactTensorQ.require(dR);
  }

  @Test
  void testAdjointCombine() {
    for (int count = 0; count < 10; ++count) {
      Tensor a = RandomSample.of(RANDOM_SAMPLE_INTERFACE);
      // LieGroupElement ga = Se2CoveringGroup.INSTANCE.element(a);
      Tensor b = RandomSample.of(RANDOM_SAMPLE_INTERFACE);
      // LieGroupElement gb = Se2CoveringGroup.INSTANCE.element(b);
      Tensor ab = Se2CoveringGroup.INSTANCE.combine(a, b);
      Tensor matrix = adjoint(Se2CoveringGroup.INSTANCE, ab);
      Tensor Ad_a = adjoint(Se2CoveringGroup.INSTANCE, a);
      Tensor Ad_b = adjoint(Se2CoveringGroup.INSTANCE, b);
      Tolerance.CHOP.requireClose(matrix, Ad_b.dot(Ad_a));
    }
  }

  @Test
  void testDLNumeric() {
    Tensor g = RandomSample.of(RANDOM_SAMPLE_INTERFACE);
    Tensor x = RandomSample.of(RANDOM_SAMPLE_INTERFACE);
    Scalar h = RealScalar.of(1e-6);
    // Tensor gexphx =
    Se2CoveringGroup.INSTANCE.combine(g, Se2CoveringGroup.INSTANCE.exponential0().exp(x.multiply(h)));
    // Tensor nu = gexphx.divide(h);
    // Tensor dL = se2CoveringGroupElement.dL(x);
    // System.out.println(nu);
    // System.out.println(dL);
  }
}

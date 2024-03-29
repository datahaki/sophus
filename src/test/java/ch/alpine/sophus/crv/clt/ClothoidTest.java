// code by jph
package ch.alpine.sophus.crv.clt;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.lie.so2.So2;
import ch.alpine.sophus.lie.so2.So2Metric;
import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Scalars;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Range;
import ch.alpine.tensor.ext.Serialization;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.nrm.Vector2Norm;
import ch.alpine.tensor.num.Pi;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.pdf.d.DiscreteUniformDistribution;
import ch.alpine.tensor.qty.Quantity;
import ch.alpine.tensor.qty.QuantityUnit;
import ch.alpine.tensor.qty.Unit;
import ch.alpine.tensor.sca.Chop;

class ClothoidTest {
  private static final ClothoidBuilder CLOTHOID_BUILDER = ClothoidBuilders.SE2_ANALYTIC.clothoidBuilder();

  @Test
  void testLength() throws ClassNotFoundException, IOException {
    Distribution distribution = NormalDistribution.standard();
    for (int count = 0; count < 100; ++count) {
      Tensor p = RandomVariate.of(distribution, 3);
      Tensor q = RandomVariate.of(distribution, 3);
      Clothoid curve = Serialization.copy(CLOTHOID_BUILDER.curve(p, q));
      Scalar length = Serialization.copy(curve).length();
      Scalar between = Vector2Norm.between(p.extract(0, 2), q.extract(0, 2));
      assertTrue(Scalars.lessEquals(between, length));
    }
  }

  @Test
  void testLengthZero() {
    Distribution distribution = NormalDistribution.standard();
    for (int count = 0; count < 100; ++count) {
      Tensor p = RandomVariate.of(distribution, 3);
      Clothoid clothoid = CLOTHOID_BUILDER.curve(p, p);
      Scalar length = clothoid.length();
      Tolerance.CHOP.requireZero(length);
    }
  }

  @Test
  void testLengthZeroExact() {
    Distribution distribution = DiscreteUniformDistribution.of(-3, +3);
    for (int count = 0; count < 100; ++count) {
      Tensor p = RandomVariate.of(distribution, 3);
      Clothoid clothoid = CLOTHOID_BUILDER.curve(p, p);
      Scalar length = clothoid.length();
      Tolerance.CHOP.requireZero(length);
    }
  }

  @Test
  void testCurvatureZero() {
    Distribution distribution = NormalDistribution.standard();
    for (int count = 0; count < 100; ++count) {
      Tensor p = RandomVariate.of(distribution, 3);
      LagrangeQuadraticD curvature = CLOTHOID_BUILDER.curve(p, p).curvature();
      curvature.head();
      // System.out.println(scalar);
      // assertFalse(NumberQ.of(scalar));
    }
  }

  @Test
  void testCurvatureZeroExact() {
    Distribution distribution = DiscreteUniformDistribution.of(-3, +3);
    for (int count = 0; count < 100; ++count) {
      Tensor p = RandomVariate.of(distribution, 3);
      LagrangeQuadraticD curvature = CLOTHOID_BUILDER.curve(p, p).curvature();
      curvature.head();
    }
  }

  @Test
  void testCurvature() throws ClassNotFoundException, IOException {
    Distribution distribution = NormalDistribution.standard();
    for (int count = 0; count < 100; ++count) {
      Tensor p = RandomVariate.of(distribution, 3);
      Tensor q = RandomVariate.of(distribution, 3);
      LagrangeQuadraticD headTailInterface = CLOTHOID_BUILDER.curve(p, q).curvature();
      Scalar head = Serialization.copy(headTailInterface).head();
      assertInstanceOf(RealScalar.class, head);
    }
  }

  @Test
  void testLengthMid() {
    Distribution distribution = NormalDistribution.standard();
    for (int count = 0; count < 100; ++count) {
      Tensor p = RandomVariate.of(distribution, 3);
      Tensor q = RandomVariate.of(distribution, 3);
      Clothoid clothoid = CLOTHOID_BUILDER.curve(p, q);
      Tensor m = clothoid.apply(RationalScalar.HALF);
      Scalar l1 = CLOTHOID_BUILDER.curve(p, m).length();
      Scalar l2 = CLOTHOID_BUILDER.curve(m, q).length();
      Chop._01.requireClose(l1, l2);
    }
  }

  @Test
  void testQuantity() {
    Clothoid clothoid = CLOTHOID_BUILDER.curve(Tensors.fromString("{1[m], 2[m], 3}"), Tensors.fromString("{7[m], -2[m], 4}"));
    Tensor tensor = clothoid.apply(RealScalar.of(0.3));
    {
      Chop._02.requireClose(tensor.extract(0, 2), Tensors.fromString("{0.8746294996808981[m], -0.3733524277684044[m]}"));
      Chop._02.requireZero(So2Metric.INSTANCE.distance(tensor.get(2), RealScalar.of(-0.4007683806054648)));
    }
    Chop._01.requireClose(clothoid.length(), Quantity.of(11.538342088739874, "m^1.0"));
    LagrangeQuadraticD curvature = clothoid.curvature();
    Chop._12.requireClose(curvature.head(), curvature.apply(RealScalar.ZERO));
    // System.out.println(curvature.tail());
    Chop._02.requireClose(curvature.head(), Quantity.of(1.1524379635834654, "m^-1"));
    Chop._12.requireClose(curvature.tail(), curvature.apply(RealScalar.ONE));
    Chop._02.requireClose(curvature.tail(), Quantity.of(-0.9791028358312921, "m^-1"));
  }

  @Test
  void testSimple() throws ClassNotFoundException, IOException {
    Tensor p = Tensors.vector(1, 2, 1);
    Tensor q = Tensors.vector(8, 6, 2);
    LagrangeQuadraticD clothoidTerminalRatio = CLOTHOID_BUILDER.curve(p, q).curvature();
    Scalar head = clothoidTerminalRatio.head();
    LagrangeQuadraticD clothoidCurvature = Serialization.copy(CLOTHOID_BUILDER.curve(p, q).curvature());
    Scalar scalar = clothoidCurvature.head();
    Chop._01.requireClose(head, scalar);
  }

  @Test
  void testStraight() {
    Tensor p = Tensors.vector(1, 2, 0);
    Tensor q = Tensors.vector(10, 2, 0);
    LagrangeQuadraticD clothoidTerminalRatio = CLOTHOID_BUILDER.curve(p, q).curvature();
    Chop._12.requireClose(clothoidTerminalRatio.head(), RealScalar.ZERO);
    Chop._12.requireClose(clothoidTerminalRatio.tail(), RealScalar.ZERO);
    LagrangeQuadraticD clothoidCurvature = CLOTHOID_BUILDER.curve(p, q).curvature();
    Chop._12.requireClose(clothoidCurvature.head(), RealScalar.ZERO);
    Chop._12.requireClose(clothoidCurvature.tail(), RealScalar.ZERO);
  }

  @Test
  void testAlmostStraight() {
    Tensor p = Tensors.vector(1, 2, 0);
    Tensor q = Tensors.vector(10, 3, 0);
    LagrangeQuadraticD lagrangeQuadraticD1 = CLOTHOID_BUILDER.curve(p, q).curvature();
    LagrangeQuadraticD lagrangeQuadraticD2 = CLOTHOID_BUILDER.curve(p, q).curvature();
    Scalar h1 = lagrangeQuadraticD1.apply(RealScalar.ZERO);
    Scalar h2 = lagrangeQuadraticD2.apply(RealScalar.ZERO);
    Chop._02.requireClose(h1, h2);
  }

  @Test
  void testSingular() {
    Tensor p = Tensors.vector(1, 2, 1);
    Tensor q = Tensors.vector(1, 2, 1);
    LagrangeQuadraticD clothoidCurvature = CLOTHOID_BUILDER.curve(p, q).curvature();
    Scalar head = clothoidCurvature.head();
    Scalar tail = clothoidCurvature.tail();
    assertTrue(Scalars.isZero(head));
    assertTrue(Scalars.isZero(tail));
  }

  @Test
  void testAngles() {
    Tensor pxy = Tensors.vector(0, 0);
    Tensor qxy = Tensors.vector(1, 0);
    Tensor angles = Range.of(-3, 4).map(Pi.TWO::multiply);
    for (Tensor angle : angles) {
      Clothoid clothoid = CLOTHOID_BUILDER.curve(pxy.append(angle), qxy.append(angle));
      Tensor r = clothoid.apply(RationalScalar.HALF);
      Chop._08.requireClose(r.extract(0, 2), Tensors.vector(0.5, 0));
      Chop._08.requireZero(So2.MOD.apply(r.Get(2)));
    }
  }

  @Test
  void testLeft() {
    LagrangeQuadraticD headTailInterface = //
        CLOTHOID_BUILDER.curve(Tensors.vector(0, 1, 0), Tensors.vector(2, 2, 0)).curvature();
    Chop._02.requireClose(headTailInterface.head(), RealScalar.of(+1.223609));
    Chop._02.requireClose(headTailInterface.tail(), RealScalar.of(-1.223609));
  }

  @Test
  void testRight() {
    LagrangeQuadraticD headTailInterface = //
        CLOTHOID_BUILDER.curve(Tensors.vector(0, 1, 0), Tensors.vector(2, 0, 0)).curvature();
    Chop._01.requireClose(headTailInterface.head(), RealScalar.of(-1.2149956565247713));
    Chop._01.requireClose(headTailInterface.tail(), RealScalar.of(+1.2149956565247713));
  }

  @Test
  void testFixedLeftUnit() {
    LagrangeQuadraticD lagrangeQuadraticD = CLOTHOID_BUILDER.curve( //
        Tensors.fromString("{0[m], 1[m], 0}"), Tensors.fromString("{2[m], 2[m], 0}")).curvature();
    Chop._01.requireClose(lagrangeQuadraticD.head(), Quantity.of(+1.2149956565247715, "m^-1")); // cl3
    Chop._01.requireClose(lagrangeQuadraticD.tail(), Quantity.of(-1.2149956565247715, "m^-1")); // cl3
    Scalar integralAbs = lagrangeQuadraticD.integralAbs();
    assertEquals(QuantityUnit.of(integralAbs), Unit.of("m^-1"));
  }

  @Test
  void testOfLeftUnit() {
    LagrangeQuadraticD clothoidTerminalRatio = CLOTHOID_BUILDER.curve( //
        Tensors.fromString("{0[m], 1[m], 0}"), Tensors.fromString("{2[m], 2[m], 0}")).curvature();
    // System.out.println(clothoidTerminalRatio.head());
    Chop._01.requireClose(clothoidTerminalRatio.head(), Quantity.of(+1.2149956565247715, "m^-1")); // cl3
    Chop._01.requireClose(clothoidTerminalRatio.tail(), Quantity.of(-1.2149956565247715, "m^-1")); // cl3
  }
}

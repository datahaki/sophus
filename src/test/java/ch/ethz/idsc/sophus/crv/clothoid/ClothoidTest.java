// code by jph
package ch.ethz.idsc.sophus.crv.clothoid;

import java.io.IOException;

import ch.ethz.idsc.sophus.lie.so2.So2;
import ch.ethz.idsc.sophus.lie.so2.So2Metric;
import ch.ethz.idsc.sophus.math.HeadTailInterface;
import ch.ethz.idsc.tensor.RationalScalar;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Scalars;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.Range;
import ch.ethz.idsc.tensor.io.Serialization;
import ch.ethz.idsc.tensor.mat.Tolerance;
import ch.ethz.idsc.tensor.opt.Pi;
import ch.ethz.idsc.tensor.pdf.DiscreteUniformDistribution;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.NormalDistribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.qty.Quantity;
import ch.ethz.idsc.tensor.red.Norm;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class ClothoidTest extends TestCase {
  public void testLength() throws ClassNotFoundException, IOException {
    Distribution distribution = NormalDistribution.standard();
    for (int count = 0; count < 100; ++count) {
      Tensor p = RandomVariate.of(distribution, 3);
      Tensor q = RandomVariate.of(distribution, 3);
      Clothoid curve = Serialization.copy(Se2Clothoids.INSTANCE.curve(p, q));
      Scalar length = Serialization.copy(curve).length();
      Scalar between = Norm._2.between(p.extract(0, 2), q.extract(0, 2));
      assertTrue(Scalars.lessEquals(between, length));
    }
  }

  public void testLengthZero() {
    Distribution distribution = NormalDistribution.standard();
    for (int count = 0; count < 100; ++count) {
      Tensor p = RandomVariate.of(distribution, 3);
      Clothoid clothoid = Se2Clothoids.INSTANCE.curve(p, p);
      Scalar length = clothoid.length();
      Tolerance.CHOP.requireZero(length);
    }
  }

  public void testLengthZeroExact() {
    Distribution distribution = DiscreteUniformDistribution.of(-3, +3);
    for (int count = 0; count < 100; ++count) {
      Tensor p = RandomVariate.of(distribution, 3);
      Clothoid clothoid = Se2Clothoids.INSTANCE.curve(p, p);
      Scalar length = clothoid.length();
      Tolerance.CHOP.requireZero(length);
    }
  }

  public void testCurvatureZero() {
    Distribution distribution = NormalDistribution.standard();
    for (int count = 0; count < 100; ++count) {
      Tensor p = RandomVariate.of(distribution, 3);
      LagrangeQuadraticD curvature = Se2Clothoids.INSTANCE.curve(p, p).curvature();
      curvature.head();
      // System.out.println(scalar);
      // assertFalse(NumberQ.of(scalar));
    }
  }

  public void testCurvatureZeroExact() {
    Distribution distribution = DiscreteUniformDistribution.of(-3, +3);
    for (int count = 0; count < 100; ++count) {
      Tensor p = RandomVariate.of(distribution, 3);
      LagrangeQuadraticD curvature = Se2Clothoids.INSTANCE.curve(p, p).curvature();
      curvature.head();
    }
  }

  public void testCurvature() throws ClassNotFoundException, IOException {
    Distribution distribution = NormalDistribution.standard();
    for (int count = 0; count < 100; ++count) {
      Tensor p = RandomVariate.of(distribution, 3);
      Tensor q = RandomVariate.of(distribution, 3);
      HeadTailInterface headTailInterface = Se2Clothoids.INSTANCE.curve(p, q).curvature();
      Scalar head = Serialization.copy(headTailInterface).head();
      assertTrue(head instanceof RealScalar);
    }
  }

  public void testLengthMid() {
    Distribution distribution = NormalDistribution.standard();
    for (int count = 0; count < 100; ++count) {
      Tensor p = RandomVariate.of(distribution, 3);
      Tensor q = RandomVariate.of(distribution, 3);
      Clothoid clothoid = Se2Clothoids.INSTANCE.curve(p, q);
      Tensor m = clothoid.apply(RationalScalar.HALF);
      Scalar l1 = Se2Clothoids.INSTANCE.curve(p, m).length();
      Scalar l2 = Se2Clothoids.INSTANCE.curve(m, q).length();
      Chop._01.requireClose(l1, l2);
    }
  }

  public void testQuantity() {
    Clothoid clothoid = Se2Clothoids.INSTANCE.curve(Tensors.fromString("{1[m], 2[m], 3}"), Tensors.fromString("{7[m], -2[m], 4}"));
    Tensor tensor = clothoid.apply(RealScalar.of(0.3));
    {
      Chop._02.requireClose(tensor.extract(0, 2), Tensors.fromString("{0.8746294996808981[m], -0.3733524277684044[m]}"));
      Chop._02.requireZero(So2Metric.INSTANCE.distance(tensor.get(2), RealScalar.of(-0.4007683806054648)));
    }
    Chop._01.requireClose(clothoid.length(), Quantity.of(11.538342088739874, "m^1.0"));
    LagrangeQuadraticD curvature = clothoid.curvature();
    Chop._12.requireClose(curvature.head(), curvature.apply(RealScalar.ZERO));
    System.out.println(curvature.tail());
    Chop._02.requireClose(curvature.head(), Quantity.of(1.1524379635834654, "m^-1"));
    Chop._12.requireClose(curvature.tail(), curvature.apply(RealScalar.ONE));
    Chop._02.requireClose(curvature.tail(), Quantity.of(-0.9791028358312921, "m^-1"));
  }

  public void testSimple() throws ClassNotFoundException, IOException {
    Tensor p = Tensors.vector(1, 2, 1);
    Tensor q = Tensors.vector(8, 6, 2);
    HeadTailInterface clothoidTerminalRatio = Se2Clothoids.INSTANCE.curve(p, q).curvature();
    Scalar head = clothoidTerminalRatio.head();
    LagrangeQuadraticD clothoidCurvature = Serialization.copy(Se2Clothoids.INSTANCE.curve(p, q).curvature());
    Scalar scalar = clothoidCurvature.head();
    Chop._01.requireClose(head, scalar);
  }

  public void testStraight() {
    Tensor p = Tensors.vector(1, 2, 0);
    Tensor q = Tensors.vector(10, 2, 0);
    HeadTailInterface clothoidTerminalRatio = Se2Clothoids.INSTANCE.curve(p, q).curvature();
    Chop._12.requireClose(clothoidTerminalRatio.head(), RealScalar.ZERO);
    Chop._12.requireClose(clothoidTerminalRatio.tail(), RealScalar.ZERO);
    LagrangeQuadraticD clothoidCurvature = Se2Clothoids.INSTANCE.curve(p, q).curvature();
    Chop._12.requireClose(clothoidCurvature.head(), RealScalar.ZERO);
    Chop._12.requireClose(clothoidCurvature.tail(), RealScalar.ZERO);
  }

  public void testAlmostStraight() {
    Tensor p = Tensors.vector(1, 2, 0);
    Tensor q = Tensors.vector(10, 3, 0);
    HeadTailInterface headTailInterface1 = Se2Clothoids.INSTANCE.curve(p, q).curvature();
    Scalar head = headTailInterface1.head();
    LagrangeQuadraticD lagrangeQuadraticD = Se2Clothoids.INSTANCE.curve(p, q).curvature();
    Scalar scalar = lagrangeQuadraticD.apply(RealScalar.ZERO);
    Chop._12.requireClose(lagrangeQuadraticD.head(), scalar);
    Chop._02.requireClose(head, scalar);
  }

  public void testSingular() {
    Tensor p = Tensors.vector(1, 2, 1);
    Tensor q = Tensors.vector(1, 2, 1);
    LagrangeQuadraticD clothoidCurvature = Se2Clothoids.INSTANCE.curve(p, q).curvature();
    Scalar head = clothoidCurvature.head();
    Scalar tail = clothoidCurvature.tail();
    assertTrue(Scalars.isZero(head));
    assertTrue(Scalars.isZero(tail));
  }

  public void testAngles() {
    Tensor pxy = Tensors.vector(0, 0);
    Tensor qxy = Tensors.vector(1, 0);
    Tensor angles = Range.of(-3, 4).map(Pi.TWO::multiply);
    for (Tensor angle : angles) {
      Clothoid clothoid = Se2Clothoids.INSTANCE.curve(pxy.append(angle), qxy.append(angle));
      Tensor r = clothoid.apply(RationalScalar.HALF);
      Chop._08.requireClose(r.extract(0, 2), Tensors.vector(0.5, 0));
      Chop._08.requireZero(So2.MOD.apply(r.Get(2)));
    }
  }

  public void testLeft() {
    HeadTailInterface headTailInterface = //
        Se2Clothoids.INSTANCE.curve(Tensors.vector(0, 1, 0), Tensors.vector(2, 2, 0)).curvature();
    Chop._02.requireClose(headTailInterface.head(), RealScalar.of(+1.223609));
    Chop._02.requireClose(headTailInterface.tail(), RealScalar.of(-1.223609));
  }

  public void testRight() {
    HeadTailInterface headTailInterface = //
        Se2Clothoids.INSTANCE.curve(Tensors.vector(0, 1, 0), Tensors.vector(2, 0, 0)).curvature();
    Chop._01.requireClose(headTailInterface.head(), RealScalar.of(-1.2149956565247713));
    Chop._01.requireClose(headTailInterface.tail(), RealScalar.of(+1.2149956565247713));
  }

  public void testFixedLeftUnit() {
    HeadTailInterface headTailInterface = Se2Clothoids.INSTANCE.curve( //
        Tensors.fromString("{0[m], 1[m], 0}"), Tensors.fromString("{2[m], 2[m], 0}")).curvature();
    Chop._01.requireClose(headTailInterface.head(), Quantity.of(+1.2149956565247715, "m^-1")); // cl3
    Chop._01.requireClose(headTailInterface.tail(), Quantity.of(-1.2149956565247715, "m^-1")); // cl3
  }

  public void testOfLeftUnit() {
    HeadTailInterface clothoidTerminalRatio = Se2Clothoids.INSTANCE.curve( //
        Tensors.fromString("{0[m], 1[m], 0}"), Tensors.fromString("{2[m], 2[m], 0}")).curvature();
    System.out.println(clothoidTerminalRatio.head());
    Chop._01.requireClose(clothoidTerminalRatio.head(), Quantity.of(+1.2149956565247715, "m^-1")); // cl3
    Chop._01.requireClose(clothoidTerminalRatio.tail(), Quantity.of(-1.2149956565247715, "m^-1")); // cl3
  }
}

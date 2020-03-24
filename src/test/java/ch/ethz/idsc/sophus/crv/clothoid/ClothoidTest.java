// code by jph
package ch.ethz.idsc.sophus.crv.clothoid;

import java.io.IOException;

import ch.ethz.idsc.sophus.crv.clothoid.Clothoid.Curvature;
import ch.ethz.idsc.sophus.math.HeadTailInterface;
import ch.ethz.idsc.tensor.NumberQ;
import ch.ethz.idsc.tensor.RationalScalar;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Scalars;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.Range;
import ch.ethz.idsc.tensor.io.Serialization;
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
      Clothoid curve = Serialization.copy(new Clothoid(p, q));
      Scalar length = Serialization.copy(curve).length();
      Scalar between = Norm._2.between(p.extract(0, 2), q.extract(0, 2));
      assertTrue(Scalars.lessEquals(between, length));
    }
  }

  public void testLengthZero() {
    Distribution distribution = NormalDistribution.standard();
    for (int count = 0; count < 100; ++count) {
      Tensor p = RandomVariate.of(distribution, 3);
      Clothoid curve = new Clothoid(p, p);
      Scalar length = curve.length();
      Chop.NONE.requireZero(length);
    }
  }

  public void testLengthZeroExact() {
    Distribution distribution = DiscreteUniformDistribution.of(-3, +3);
    for (int count = 0; count < 100; ++count) {
      Tensor p = RandomVariate.of(distribution, 3);
      Clothoid curve = new Clothoid(p, p);
      Scalar length = curve.length();
      Chop.NONE.requireZero(length);
    }
  }

  public void testCurvatureZero() {
    Distribution distribution = NormalDistribution.standard();
    for (int count = 0; count < 100; ++count) {
      Tensor p = RandomVariate.of(distribution, 3);
      Curvature curvature = new Clothoid(p, p).new Curvature();
      Scalar scalar = curvature.head();
      assertFalse(NumberQ.of(scalar));
    }
  }

  public void testCurvatureZeroExact() {
    Distribution distribution = DiscreteUniformDistribution.of(-3, +3);
    for (int count = 0; count < 100; ++count) {
      Tensor p = RandomVariate.of(distribution, 3);
      Curvature curvature = new Clothoid(p, p).new Curvature();
      Scalar scalar = curvature.head();
      assertFalse(NumberQ.of(scalar));
    }
  }

  public void testCurvature() throws ClassNotFoundException, IOException {
    Distribution distribution = NormalDistribution.standard();
    for (int count = 0; count < 100; ++count) {
      Tensor p = RandomVariate.of(distribution, 3);
      Tensor q = RandomVariate.of(distribution, 3);
      Curvature curvature = new Clothoid(p, q).new Curvature();
      Scalar head = Serialization.copy(curvature).head();
      assertTrue(head instanceof RealScalar);
    }
  }

  public void testLengthMid() {
    Distribution distribution = NormalDistribution.standard();
    for (int count = 0; count < 100; ++count) {
      Tensor p = RandomVariate.of(distribution, 3);
      Tensor q = RandomVariate.of(distribution, 3);
      Clothoid curve = new Clothoid(p, q);
      Tensor m = curve.apply(RationalScalar.HALF);
      {
        Scalar l1 = new Clothoid(p, m).length();
        Scalar l2 = new Clothoid(m, q).length();
        Chop._01.requireClose(l1, l2);
      }
    }
  }

  public void testQuantity() {
    Clothoid clothoid = new Clothoid(Tensors.fromString("{1[m], 2[m], 3}"), Tensors.fromString("{7[m], -2[m], 4}"));
    Tensor tensor = clothoid.apply(RealScalar.of(0.3));
    Chop._08.requireClose(tensor, Tensors.fromString("{0.8746294996808981[m], -0.3733524277684044[m], -0.4007683806054648}"));
    Chop._10.requireClose(clothoid.length(), Quantity.of(11.538342088739874, "m^1.0"));
    Curvature curvature = clothoid.new Curvature();
    Chop._12.requireClose(curvature.head(), curvature.apply(RealScalar.ZERO));
    System.out.println(curvature.tail());
    Chop._10.requireClose(curvature.head(), Quantity.of(1.1524379635834654, "m^-1"));
    Chop._12.requireClose(curvature.tail(), curvature.apply(RealScalar.ONE));
    Chop._10.requireClose(curvature.tail(), Quantity.of(-0.9791028358312921, "m^-1"));
  }

  public void testSimple() throws ClassNotFoundException, IOException {
    Tensor p = Tensors.vector(1, 2, 1);
    Tensor q = Tensors.vector(8, 6, 2);
    HeadTailInterface clothoidTerminalRatio = new Clothoid(p, q).new Curvature();
    Scalar head = clothoidTerminalRatio.head();
    Curvature clothoidCurvature = Serialization.copy(new Clothoid(p, q).new Curvature());
    Scalar scalar = clothoidCurvature.head();
    Chop._01.requireClose(head, scalar);
  }

  public void testStraight() {
    Tensor p = Tensors.vector(1, 2, 0);
    Tensor q = Tensors.vector(10, 2, 0);
    HeadTailInterface clothoidTerminalRatio = new Clothoid(p, q).new Curvature();
    Chop._12.requireClose(clothoidTerminalRatio.head(), RealScalar.ZERO);
    Chop._12.requireClose(clothoidTerminalRatio.tail(), RealScalar.ZERO);
    Curvature clothoidCurvature = new Clothoid(p, q).new Curvature();
    Chop._12.requireClose(clothoidCurvature.head(), RealScalar.ZERO);
    Chop._12.requireClose(clothoidCurvature.tail(), RealScalar.ZERO);
  }

  public void testAlmostStraight() {
    Tensor p = Tensors.vector(1, 2, 0);
    Tensor q = Tensors.vector(10, 3, 0);
    HeadTailInterface clothoidTerminalRatio = new Clothoid(p, q).new Curvature();
    Scalar head = clothoidTerminalRatio.head();
    Curvature clothoidCurvature = new Clothoid(p, q).new Curvature();
    Scalar scalar = clothoidCurvature.apply(RealScalar.ZERO);
    Chop._12.requireClose(clothoidCurvature.head(), scalar);
    Chop._02.requireClose(head, scalar);
  }

  public void testSingular() {
    Tensor p = Tensors.vector(1, 2, 1);
    Tensor q = Tensors.vector(1, 2, 1);
    Curvature clothoidCurvature = new Clothoid(p, q).new Curvature();
    Scalar head = clothoidCurvature.head();
    Scalar tail = clothoidCurvature.tail();
    assertFalse(NumberQ.of(head));
    assertFalse(NumberQ.of(tail));
  }

  public void testAngles() {
    Tensor pxy = Tensors.vector(0, 0);
    Tensor qxy = Tensors.vector(1, 0);
    Tensor angles = Range.of(-3, 4).map(Pi.TWO::multiply);
    for (Tensor angle : angles) {
      Clothoid clothoid = new Clothoid(pxy.append(angle), qxy.append(angle));
      Tensor r = clothoid.apply(RationalScalar.HALF);
      Chop._13.requireClose(r, Tensors.vector(0.5, 0, 0));
    }
  }

  public void testLeft() {
    HeadTailInterface headTailInterface = //
        new Clothoid(Tensors.vector(0, 1, 0), Tensors.vector(2, 2, 0)).new Curvature();
    Chop._10.requireClose(headTailInterface.head(), RealScalar.of(+1.2149956565247715));
    Chop._10.requireClose(headTailInterface.tail(), RealScalar.of(-1.2149956565247715));
  }

  public void testRight() {
    HeadTailInterface headTailInterface = //
        new Clothoid(Tensors.vector(0, 1, 0), Tensors.vector(2, 0, 0)).new Curvature();
    Chop._10.requireClose(headTailInterface.head(), RealScalar.of(-1.2149956565247713));
    Chop._10.requireClose(headTailInterface.tail(), RealScalar.of(+1.2149956565247713));
  }

  public void testFixedLeftUnit() {
    HeadTailInterface headTailInterface = new Clothoid( //
        Tensors.fromString("{0[m], 1[m], 0}"), Tensors.fromString("{2[m], 2[m], 0}")).new Curvature();
    Chop._10.requireClose(headTailInterface.head(), Quantity.of(+1.2149956565247715, "m^-1")); // cl3
    Chop._10.requireClose(headTailInterface.tail(), Quantity.of(-1.2149956565247715, "m^-1")); // cl3
  }

  public void testOfLeftUnit() {
    HeadTailInterface clothoidTerminalRatio = new Clothoid( //
        Tensors.fromString("{0[m], 1[m], 0}"), Tensors.fromString("{2[m], 2[m], 0}")).new Curvature();
    System.out.println(clothoidTerminalRatio.head());
    Chop._10.requireClose(clothoidTerminalRatio.head(), Quantity.of(+1.2149956565247715, "m^-1")); // cl3
    Chop._10.requireClose(clothoidTerminalRatio.tail(), Quantity.of(-1.2149956565247715, "m^-1")); // cl3
  }
}

// code by jph
package ch.ethz.idsc.sophus.crv.clothoid;

import ch.ethz.idsc.sophus.lie.se2.Se2GroupElement;
import ch.ethz.idsc.sophus.lie.so2.So2;
import ch.ethz.idsc.sophus.math.HeadTailInterface;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.opt.Pi;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.NormalDistribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class ClothoidIntegralTest extends TestCase {
  public void testReverse() {
    Distribution distribution = NormalDistribution.of(0, 3);
    for (int count = 0; count < 100; ++count) {
      Tensor p = RandomVariate.of(distribution, 3);
      Tensor q = RandomVariate.of(distribution, 3);
      HeadTailInterface headTailInterface1 = Se2Clothoids.INSTANCE.curve(p, q).curvature();
      p.set(s -> So2.MOD.apply(Pi.VALUE.add(s)), 2);
      q.set(s -> So2.MOD.apply(Pi.VALUE.add(s)), 2);
      HeadTailInterface headTailInterface2 = Se2Clothoids.INSTANCE.curve(q, p).curvature();
      Chop._06.requireClose(headTailInterface1.head(), headTailInterface2.tail().negate());
      Chop._06.requireClose(headTailInterface1.tail(), headTailInterface2.head().negate());
    }
  }

  public void testAction() {
    Distribution distribution = NormalDistribution.of(0, 3);
    for (int count = 0; count < 100; ++count) {
      Tensor p = RandomVariate.of(distribution, 3);
      Tensor q = RandomVariate.of(distribution, 3);
      HeadTailInterface headTailInterface1 = Se2Clothoids.INSTANCE.curve(p, q).curvature();
      Tensor g = RandomVariate.of(distribution, 3);
      Se2GroupElement se2GroupElement = new Se2GroupElement(g);
      HeadTailInterface headTailInterface2 = Se2Clothoids.INSTANCE.curve( //
          se2GroupElement.combine(p), //
          se2GroupElement.combine(q)).curvature();
      Chop._06.requireClose(headTailInterface1.head(), headTailInterface2.head());
      Chop._06.requireClose(headTailInterface1.tail(), headTailInterface2.tail());
    }
  }

  public void testTwoPi() {
    Distribution distribution = NormalDistribution.of(0, 3);
    for (int count = 0; count < 100; ++count) {
      Tensor p = RandomVariate.of(distribution, 3);
      Tensor q = RandomVariate.of(distribution, 3);
      HeadTailInterface headTailInterface1 = Se2Clothoids.INSTANCE.curve(p, q).curvature();
      p.set(Pi.TWO::add, 2);
      q.set(Pi.TWO::add, 2);
      HeadTailInterface headTailInterface2 = Se2Clothoids.INSTANCE.curve(p, q).curvature();
      Chop._10.requireClose(headTailInterface1.head(), headTailInterface2.head());
      Chop._10.requireClose(headTailInterface1.tail(), headTailInterface2.tail());
    }
  }

  public void testStraightSide() {
    Tensor p = Tensors.vector(0, 0, 0);
    Tensor q = Tensors.vector(3, 0, 0);
    {
      HeadTailInterface headTailInterface = Se2Clothoids.INSTANCE.curve(p, q).curvature();
      Chop._03.requireClose(headTailInterface.head(), RealScalar.ZERO);
      Chop._03.requireClose(headTailInterface.tail(), RealScalar.ZERO);
      assertNotNull(headTailInterface.toString());
    }
  }

  public void testStraightUp() {
    Tensor p = Tensors.vector(0, 0, +Math.PI / 2);
    Tensor q = Tensors.vector(0, 3, +Math.PI / 2);
    HeadTailInterface headTailInterface = Se2Clothoids.INSTANCE.curve(p, q).curvature();
    Chop._03.requireClose(headTailInterface.head(), RealScalar.ZERO);
    Chop._03.requireClose(headTailInterface.tail(), RealScalar.ZERO);
  }

  public void testCircle() {
    Tensor p = Tensors.vector(0, 0, +Math.PI / 2);
    Tensor q = Tensors.vector(-2, 0, -Math.PI / 2);
    HeadTailInterface headTailInterface = Se2Clothoids.INSTANCE.curve(p, q).curvature();
    Chop._03.requireClose(headTailInterface.head(), RealScalar.ONE);
    Chop._03.requireClose(headTailInterface.tail(), RealScalar.ONE);
  }
}

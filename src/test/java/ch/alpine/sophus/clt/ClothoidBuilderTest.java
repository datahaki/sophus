// code by jph
package ch.alpine.sophus.clt;

import ch.alpine.sophus.lie.se2.Se2GroupElement;
import ch.alpine.sophus.lie.so2.So2;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.VectorQ;
import ch.alpine.tensor.num.Pi;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.sca.Chop;
import junit.framework.TestCase;

public class ClothoidBuilderTest extends TestCase {
  private static final ClothoidBuilder CLOTHOID_BUILDER = ClothoidBuilders.SE2_ANALYTIC.clothoidBuilder();

  public void testReverse() {
    Distribution distribution = NormalDistribution.of(0, 3);
    for (int count = 0; count < 100; ++count) {
      Tensor p = RandomVariate.of(distribution, 3);
      Tensor q = RandomVariate.of(distribution, 3);
      Clothoid clothoid = CLOTHOID_BUILDER.curve(p, q);
      Tensor tensor = CLOTHOID_BUILDER.split(p, q, RealScalar.of(0.2));
      VectorQ.requireLength(tensor, 3);
      LagrangeQuadraticD lagrangeQuadraticD1 = clothoid.curvature();
      p.set(s -> So2.MOD.apply(Pi.VALUE.add(s)), 2);
      q.set(s -> So2.MOD.apply(Pi.VALUE.add(s)), 2);
      LagrangeQuadraticD lagrangeQuadraticD2 = CLOTHOID_BUILDER.curve(q, p).curvature();
      Scalar param = RandomVariate.of(distribution);
      Chop._06.requireClose( //
          lagrangeQuadraticD1.apply(param), //
          lagrangeQuadraticD2.apply(RealScalar.ONE.subtract(param)).negate());
    }
  }

  public void testAction() {
    Distribution distribution = NormalDistribution.of(0, 3);
    for (int count = 0; count < 100; ++count) {
      Tensor p = RandomVariate.of(distribution, 3);
      Tensor q = RandomVariate.of(distribution, 3);
      LagrangeQuadraticD lagrangeQuadraticD1 = CLOTHOID_BUILDER.curve(p, q).curvature();
      Tensor g = RandomVariate.of(distribution, 3);
      Se2GroupElement se2GroupElement = new Se2GroupElement(g);
      LagrangeQuadraticD lagrangeQuadraticD2 = CLOTHOID_BUILDER.curve( //
          se2GroupElement.combine(p), //
          se2GroupElement.combine(q)).curvature();
      Scalar param = RandomVariate.of(distribution);
      Chop._06.requireClose( //
          lagrangeQuadraticD1.apply(param), //
          lagrangeQuadraticD2.apply(param));
    }
  }

  public void testTwoPi() {
    Distribution distribution = NormalDistribution.of(0, 3);
    for (int count = 0; count < 100; ++count) {
      Tensor p = RandomVariate.of(distribution, 3);
      Tensor q = RandomVariate.of(distribution, 3);
      LagrangeQuadraticD lagrangeQuadraticD1 = CLOTHOID_BUILDER.curve(p, q).curvature();
      p.set(Pi.TWO::add, 2);
      q.set(Pi.TWO::add, 2);
      LagrangeQuadraticD lagrangeQuadraticD2 = CLOTHOID_BUILDER.curve(p, q).curvature();
      Scalar param = RandomVariate.of(distribution);
      Chop._06.requireClose( // 1e-10 does not always work
          lagrangeQuadraticD1.apply(param), //
          lagrangeQuadraticD2.apply(param));
    }
  }

  public void testStraightSide() {
    Tensor p = Tensors.vector(0, 0, 0);
    Tensor q = Tensors.vector(3, 0, 0);
    LagrangeQuadraticD lagrangeQuadraticD = CLOTHOID_BUILDER.curve(p, q).curvature();
    Chop._03.requireClose(lagrangeQuadraticD.head(), RealScalar.ZERO);
    Chop._03.requireClose(lagrangeQuadraticD.tail(), RealScalar.ZERO);
    assertNotNull(lagrangeQuadraticD.toString());
  }

  public void testStraightUp() {
    Tensor p = Tensors.vector(0, 0, +Math.PI / 2);
    Tensor q = Tensors.vector(0, 3, +Math.PI / 2);
    LagrangeQuadraticD headTailInterface = CLOTHOID_BUILDER.curve(p, q).curvature();
    Chop._03.requireClose(headTailInterface.head(), RealScalar.ZERO);
    Chop._03.requireClose(headTailInterface.tail(), RealScalar.ZERO);
  }

  public void testCircle() {
    Tensor p = Tensors.vector(0, 0, +Math.PI / 2);
    Tensor q = Tensors.vector(-2, 0, -Math.PI / 2);
    LagrangeQuadraticD headTailInterface = CLOTHOID_BUILDER.curve(p, q).curvature();
    Chop._03.requireClose(headTailInterface.head(), RealScalar.ONE);
    Chop._03.requireClose(headTailInterface.tail(), RealScalar.ONE);
  }
}

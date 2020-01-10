// code by jph
package ch.ethz.idsc.sophus.crv.clothoid;

import ch.ethz.idsc.sophus.crv.subdiv.CurveSubdivision;
import ch.ethz.idsc.sophus.crv.subdiv.LaneRiesenfeldCurveSubdivision;
import ch.ethz.idsc.sophus.math.HeadTailInterface;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.io.TableBuilder;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.NormalDistribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.pdf.UniformDistribution;
import ch.ethz.idsc.tensor.qty.Quantity;
import ch.ethz.idsc.tensor.red.Nest;
import ch.ethz.idsc.tensor.sca.Chop;
import ch.ethz.idsc.tensor.sca.Round;
import junit.framework.TestCase;

public class ClothoidTerminalRatiosTest extends TestCase {
  public void testLeft() {
    HeadTailInterface clothoidTerminalRatio = ClothoidTerminalRatios.fixed( //
        Tensors.vector(0, 1, 0), Tensors.vector(2, 2, 0), 3);
    // turn left
    // Chop._10.requireClose(clothoidTerminalRatios.head(), RealScalar.of(+0.9068461106738649)); // cl1
    Chop._10.requireClose(clothoidTerminalRatio.head(), RealScalar.of(+0.9114223615997659)); // cl3
    // turn right
    // Chop._10.requireClose(clothoidTerminalRatios.tail(), RealScalar.of(-0.9068461106738649)); // cl1
    Chop._10.requireClose(clothoidTerminalRatio.tail(), RealScalar.of(-0.9114223615997659)); // cl3
  }

  public void testLeftUniv() {
    HeadTailInterface clothoidTerminalRatio = ClothoidTerminalRatios.planar( //
        Tensors.vector(0, 1, 0).unmodifiable(), //
        Tensors.vector(2, 2, 0).unmodifiable());
    // turn left
    // Chop._08.requireClose(clothoidTerminalRatios.head(), RealScalar.of(+1.2190137723033907)); // cl1
    Chop._08.requireClose(clothoidTerminalRatio.head(), RealScalar.of(+1.2148814483313415)); // cl3
    // turn right
    // Chop._08.requireClose(clothoidTerminalRatios.tail(), RealScalar.of(-1.2190137715979599)); // cl1
    Chop._08.requireClose(clothoidTerminalRatio.tail(), RealScalar.of(-1.2148814483313415)); // cl3
  }

  public void testRight() {
    HeadTailInterface clothoidTerminalRatio = ClothoidTerminalRatios.fixed( //
        Tensors.vector(0, 1, 0), Tensors.vector(2, 0, 0), 3);
    // turn right
    // Chop._10.requireClose(clothoidTerminalRatios.head(), RealScalar.of(-0.9068461106738649)); // cl1
    Chop._10.requireClose(clothoidTerminalRatio.head(), RealScalar.of(-0.9114223615997659)); // cl3
    // turn left
    // Chop._10.requireClose(clothoidTerminalRatios.tail(), RealScalar.of(+0.9068461106738649)); // cl1
    Chop._10.requireClose(clothoidTerminalRatio.tail(), RealScalar.of(+0.9114223615997659)); // cl3
  }

  public void testFixedLeftUnit() {
    HeadTailInterface clothoidTerminalRatio = ClothoidTerminalRatios.fixed( //
        Tensors.fromString("{0[m], 1[m], 0}"), Tensors.fromString("{2[m], 2[m], 0}"), 3);
    // turn left
    // Chop._10.requireClose(clothoidTerminalRatios.head(), Quantity.of(+0.9068461106738649, "m^-1")); // cl1
    Chop._10.requireClose(clothoidTerminalRatio.head(), Quantity.of(+0.9114223615997659, "m^-1")); // cl3
    // turn right
    // Chop._10.requireClose(clothoidTerminalRatios.tail(), Quantity.of(-0.9068461106738649, "m^-1")); // cl1
    Chop._10.requireClose(clothoidTerminalRatio.tail(), Quantity.of(-0.9114223615997659, "m^-1")); // cl3
  }

  public void testOfLeftUnit() {
    HeadTailInterface clothoidTerminalRatio = ClothoidTerminalRatios.of( //
        Tensors.fromString("{0[m], 1[m], 0}"), Tensors.fromString("{2[m], 2[m], 0}"), 3);
    // turn left
    // Chop._10.requireClose(clothoidTerminalRatios.head(), Quantity.of(+0.9068461106738649, "m^-1")); // cl1
    Chop._10.requireClose(clothoidTerminalRatio.head(), Quantity.of(+1.2201535736244924, "m^-1")); // cl3
    // turn right
    // Chop._10.requireClose(clothoidTerminalRatios.tail(), Quantity.of(-0.9068461106738649, "m^-1")); // cl1
    Chop._10.requireClose(clothoidTerminalRatio.tail(), Quantity.of(-1.2201535736244924, "m^-1")); // cl3
  }

  public void testCurve() {
    Distribution distribution = NormalDistribution.standard();
    CurveSubdivision curveSubdivision = LaneRiesenfeldCurveSubdivision.of(Clothoids.INSTANCE, 1);
    for (int depth = 2; depth < 5; ++depth)
      for (int count = 0; count < 10; ++count) {
        Tensor beg = RandomVariate.of(distribution, 3);
        Tensor end = RandomVariate.of(distribution, 3);
        Tensor init = Tensors.of(beg, end);
        Tensor curve = Nest.of(curveSubdivision::string, init, depth);
        Scalar head = ClothoidTerminalRatios.curvature(curve.extract(0, 3));
        Scalar tail = ClothoidTerminalRatios.curvature(curve.extract(curve.length() - 3, curve.length()));
        HeadTailInterface clothoidTerminalRatio = //
            ClothoidTerminalRatios.fixed(beg.unmodifiable(), end.unmodifiable(), depth);
        assertEquals(head, clothoidTerminalRatio.head());
        assertEquals(tail, clothoidTerminalRatio.tail());
      }
  }

  public void testPercision() {
    TableBuilder tableBuilder = new TableBuilder();
    for (int depth = 5; depth < ClothoidTerminalRatios.MAX_ITER; ++depth) {
      HeadTailInterface clothoidTerminalRatio = //
          ClothoidTerminalRatios.fixed(Tensors.vector(0, 1, 0).unmodifiable(), Tensors.vector(2, 0, 0).unmodifiable(), depth);
      tableBuilder.appendRow(RealScalar.of(depth), clothoidTerminalRatio.head().map(Round._8), clothoidTerminalRatio.tail().map(Round._8));
    }
    // System.out.println(MatrixForm.of(tableBuilder.toTable()));
  }

  private static void _checkZero(Tensor pose, Scalar zero) {
    {
      HeadTailInterface clothoidTerminalRatio = ClothoidTerminalRatios.fixed(pose, pose, 3);
      assertEquals(clothoidTerminalRatio.head(), zero);
      assertEquals(clothoidTerminalRatio.tail(), zero);
    }
    {
      HeadTailInterface clothoidTerminalRatio = ClothoidTerminalRatios.planar(pose, pose);
      assertEquals(clothoidTerminalRatio.head(), zero);
      assertEquals(clothoidTerminalRatio.tail(), zero);
    }
  }

  public void testSame() {
    _checkZero(Tensors.vector(1, 1, 1), RealScalar.ZERO);
    _checkZero(Tensors.fromString("{1[m], 1[m], 1}"), Quantity.of(0, "m^-1"));
    _checkZero(RandomVariate.of(UniformDistribution.unit(), 3), Quantity.of(0, ""));
    _checkZero(Tensors.fromString("{1.1[m], 1.2[m], 1.3}"), Quantity.of(0, "m^-1"));
  }

  public void testOpenEnd() {
    Distribution distribution = NormalDistribution.standard();
    Chop chop = Chop._02;
    int failCount = 0;
    for (int count = 0; count < 20; ++count) {
      Tensor beg = RandomVariate.of(distribution, 3);
      Tensor end = RandomVariate.of(distribution, 3);
      HeadTailInterface clothoidTerminalRatio1 = ClothoidTerminalRatios.planar(beg, end);
      HeadTailInterface clothoidTerminalRatio2 = ClothoidTerminalRatios.fixed(beg, end, ClothoidTerminalRatios.MAX_ITER);
      if (!chop.close(clothoidTerminalRatio1.head(), clothoidTerminalRatio2.head())) {
        // beg={0.33199331585891245, -0.553240463025886, -0.03881900926835866}
        // end={-1.3174375242633647, -0.9411502957371748, 0.25948643363292373}
        System.out.println("beg=" + beg);
        System.out.println("end=" + end);
        Scalar err = clothoidTerminalRatio1.head().subtract(clothoidTerminalRatio2.head()).abs();
        System.out.println("err=" + err);
        // chop.requireClose(clothoidTerminalRatios1.head(), clothoidTerminalRatios2.head());
        ++failCount;
      }
    }
    if (10 < failCount)
      fail();
  }

  public void testOpenEndUnit() {
    HeadTailInterface clothoidTerminalRatio1 = ClothoidTerminalRatios.planar( //
        Tensors.fromString("{1[m], 1[m], 1}"), //
        Tensors.fromString("{2[m], 3[m], 3}"));
    HeadTailInterface clothoidTerminalRatio2 = ClothoidTerminalRatios.fixed( //
        Tensors.fromString("{1[m], 1[m], 1}"), //
        Tensors.fromString("{2[m], 3[m], 3}"), ClothoidTerminalRatios.MAX_ITER);
    ClothoidTerminalRatios.CHOP.requireClose(clothoidTerminalRatio1.head(), clothoidTerminalRatio2.head());
  }

  public void testMaxIter() {
    assertTrue(ClothoidTerminalRatios.MAX_ITER <= 20);
  }

  public void testAnalytic() {
    Tensor p = Tensors.vector(0, 1, 0).unmodifiable();
    Tensor q = Tensors.vector(2, 2, 0).unmodifiable();
    HeadTailInterface clothoidTerminalRatio1 = ClothoidTerminalRatios.planar(p, q);
    HeadTailInterface clothoidTerminalRatio2 = ClothoidTerminalRatios.of(p, q, 10);
    // turn left
    // Chop._08.requireClose(clothoidTerminalRatios.head(), RealScalar.of(+1.2190137723033907)); // cl1
    Chop._08.requireClose(clothoidTerminalRatio1.head(), RealScalar.of(+1.2148814483313415)); // cl3
    // Chop._05.requireClose(clothoidTerminalRatio1.head(), clothoidTerminalRatio2.head());
    Chop._02.requireClose(clothoidTerminalRatio1.head(), clothoidTerminalRatio2.head());
    // turn right
    // Chop._08.requireClose(clothoidTerminalRatios.tail(), RealScalar.of(-1.2190137715979599)); // cl1
    Chop._08.requireClose(clothoidTerminalRatio1.tail(), RealScalar.of(-1.2148814483313415)); // cl3
    Chop._02.requireClose(clothoidTerminalRatio1.tail(), clothoidTerminalRatio2.tail());
    // Chop._03.requireClose(clothoidTerminalRatios.head(), ClothoidTerminalRatios2.head(p, q));
    // Chop._03.requireClose(clothoidTerminalRatios.tail(), ClothoidTerminalRatios2.tail(p, q));
  }

  public void testDepthZeroFail() {
    try {
      ClothoidTerminalRatios.fixed(Tensors.vector(0, 1, 0), Tensors.vector(2, 0, 0), 0);
      fail();
    } catch (Exception exception) {
      // ---
    }
  }
}

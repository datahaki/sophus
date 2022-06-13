// code by jph
package ch.alpine.sophus.crv.clt;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.lie.so2.So2;
import ch.alpine.sophus.ref.d1.CurveSubdivision;
import ch.alpine.sophus.ref.d1.LaneRiesenfeldCurveSubdivision;
import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Array;
import ch.alpine.tensor.api.ScalarTensorFunction;
import ch.alpine.tensor.ext.Serialization;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.red.Nest;
import ch.alpine.tensor.sca.Chop;

class ClothoidBuilderImplTest {
  private static final ClothoidBuilder CLOTHOID_BUILDER = ClothoidBuilders.SE2_ANALYTIC.clothoidBuilder();

  @Test
  void testSimple() throws ClassNotFoundException, IOException {
    Distribution distribution = UniformDistribution.of(-8, 8);
    ClothoidBuilder clothoidInterface = Serialization.copy(CLOTHOID_BUILDER);
    for (int count = 0; count < 100; ++count) {
      Tensor p = RandomVariate.of(distribution, 3);
      Tensor q = RandomVariate.of(distribution, 3);
      Clothoid clothoid = clothoidInterface.curve(p, q);
      Chop._07.requireZero(So2.MOD.apply(clothoid.apply(RealScalar.ZERO).Get(2).subtract(p.Get(2))));
      Chop._07.requireZero(So2.MOD.apply(clothoid.apply(RealScalar.ONE).Get(2).subtract(q.Get(2))));
    }
  }

  @Test
  void testErf() {
    ScalarTensorFunction scalarTensorFunction = CLOTHOID_BUILDER.curve(Tensors.vector(1, 2, 3), Array.zeros(3));
    assertInstanceOf(Clothoid.class, scalarTensorFunction);
  }

  @SuppressWarnings("unused")
  @Test
  void testSimple2() {
    Tensor q = Tensors.vector(-2.05, 0, 0);
    CurveSubdivision lrL = LaneRiesenfeldCurveSubdivision.of(ClothoidBuilders.SE2_LEGENDRE.clothoidBuilder(), 1);
    CurveSubdivision lrA = LaneRiesenfeldCurveSubdivision.of(ClothoidBuilders.SE2_ANALYTIC.clothoidBuilder(), 1);
    {
      Tensor pL = lrL.string(Tensors.of(Array.zeros(3), q)).get(1);
      Chop._12.requireClose(pL, Tensors.vector(-1.025, 0, 4.245082897851892));
      Tensor mLA = lrA.string(Tensors.of(pL, q)).get(1);
      Tensor mLL = lrL.string(Tensors.of(pL, q)).get(1);
      Chop._02.requireClose(mLA, mLL);
      // System.out.println(mLL);
      // {-1.7030138036773317, 0.2166473557662918, 2.430899623066361}
      Tensor mLO = ComplexClothoidCurve.INSTANCE.curve(pL, q).apply(RationalScalar.HALF);
      // System.out.println(mLO);
      Tensor mL3 = new ClothoidCurve3(pL, q).apply(RationalScalar.HALF);
      // System.out.println(mL3);
      // System.out.println("---");
    }
    {
      Tensor pA = lrA.string(Tensors.of(Array.zeros(3), q)).get(1);
      Chop._12.requireClose(pA, Tensors.vector(-1.025, 0, 4.245082897851892));
      Tensor mAA = lrA.string(Tensors.of(pA, q)).get(1);
      Chop._05.requireClose(mAA, Tensors.vector(-1.8944972463160186, -0.7629704708387287, 3.1340144486447543));
      Tensor mAL = lrL.string(Tensors.of(pA, q)).get(1);
      Chop._02.requireClose(mAA, mAL);
      // System.out.println(mAA);
      // {-1.8944972463160186, -0.7629704708387287, 3.1340144486447543}
      // Tensor mAO = ComplexClothoidCurve.INSTANCE.curve(pA, q).apply(RationalScalar.HALF);
      // System.out.println(mAO);
      // Tensor mA3 = new ClothoidCurve3(pA, q).apply(RationalScalar.HALF);
      // System.out.println(mA3);
      // System.out.println("---");
    }
    {
      Tensor subL = Nest.of(lrL::string, Tensors.of(Array.zeros(3), q), 2);
      // System.out.println(Pretty.of(subL.map(Round._4)));
    }
    {
      Tensor subA = Nest.of(lrA::string, Tensors.of(Array.zeros(3), q), 2);
      // System.out.println(Pretty.of(subA.map(Round._4)));
    }
  }
}

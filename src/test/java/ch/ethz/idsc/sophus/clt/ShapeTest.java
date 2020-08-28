// code by jph
package ch.ethz.idsc.sophus.clt;

import ch.ethz.idsc.sophus.ref.d1.CurveSubdivision;
import ch.ethz.idsc.sophus.ref.d1.LaneRiesenfeldCurveSubdivision;
import ch.ethz.idsc.tensor.RationalScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.Array;
import ch.ethz.idsc.tensor.io.Pretty;
import ch.ethz.idsc.tensor.red.Nest;
import ch.ethz.idsc.tensor.sca.Chop;
import ch.ethz.idsc.tensor.sca.Round;
import junit.framework.TestCase;

public class ShapeTest extends TestCase {
  public void testSimple() {
    Tensor q = Tensors.vector(-2.05, 0, 0);
    CurveSubdivision lrL = LaneRiesenfeldCurveSubdivision.of(ClothoidBuilders.SE2_LEGENDRE, 1);
    CurveSubdivision lrA = LaneRiesenfeldCurveSubdivision.of(ClothoidBuilders.SE2_ANALYTIC, 1);
    {
      Tensor pL = lrL.string(Tensors.of(Array.zeros(3), q)).get(1);
      Chop._12.requireClose(pL, Tensors.vector(-1.025, 0, 4.245082897851892));
      Tensor mLA = lrA.string(Tensors.of(pL, q)).get(1);
      Tensor mLL = lrL.string(Tensors.of(pL, q)).get(1);
      Chop._02.requireClose(mLA, mLL);
      System.out.println(mLL);
      // {-1.7030138036773317, 0.2166473557662918, 2.430899623066361}
      Tensor mLO = ComplexClothoidCurve.INSTANCE.curve(pL, q).apply(RationalScalar.HALF);
      System.out.println(mLO);
      Tensor mL3 = new ClothoidCurve3(pL, q).apply(RationalScalar.HALF);
      System.out.println(mL3);
      System.out.println("---");
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
      System.out.println(Pretty.of(subL.map(Round._4)));
    }
    {
      Tensor subA = Nest.of(lrA::string, Tensors.of(Array.zeros(3), q), 2);
      System.out.println(Pretty.of(subA.map(Round._4)));
    }
  }
}

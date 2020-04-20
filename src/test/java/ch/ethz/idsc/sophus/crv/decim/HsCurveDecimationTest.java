// code by jph
package ch.ethz.idsc.sophus.crv.decim;

import ch.ethz.idsc.sophus.lie.rn.RnManifold;
import ch.ethz.idsc.sophus.lie.se2.Se2Manifold;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.pdf.UniformDistribution;
import ch.ethz.idsc.tensor.qty.Quantity;
import junit.framework.TestCase;

public class HsCurveDecimationTest extends TestCase {
  public void testSimple() {
    for (HsCurveDecimation hsCurveDecimation : HsCurveDecimation.values()) {
      CurveDecimation curveDecimation = //
          hsCurveDecimation.of(Se2Manifold.INSTANCE, Se2Manifold.HS_EXP, RealScalar.of(0.4));
      Tensor tensor = curveDecimation.apply(RandomVariate.of(UniformDistribution.unit(), 100, 3));
      assertTrue(tensor.length() < 100);
    }
  }

  public void testQuantity() {
    for (HsCurveDecimation hsCurveDecimation : HsCurveDecimation.values()) {
      CurveDecimation curveDecimation = //
          hsCurveDecimation.of(RnManifold.INSTANCE, RnManifold.HS_EXP, Quantity.of(0.7, "m"));
      Tensor tensor = curveDecimation.apply(RandomVariate.of(UniformDistribution.unit(), 100, 3).map(s -> Quantity.of(s, "m")));
      assertTrue(tensor.length() < 90);
    }
  }
}

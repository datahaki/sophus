// code by jph
package ch.alpine.sophus.decim;

import ch.alpine.sophus.lie.rn.RnManifold;
import ch.alpine.sophus.lie.se2.Se2Manifold;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.UniformDistribution;
import ch.alpine.tensor.qty.Quantity;
import junit.framework.TestCase;

public class LineDistancesTest extends TestCase {
  public void testSimple() {
    for (LineDistances lineDistances : LineDistances.values()) {
      CurveDecimation curveDecimation = CurveDecimation.of( //
          lineDistances.supply(Se2Manifold.INSTANCE), RealScalar.of(0.4));
      Tensor tensor = curveDecimation.apply(RandomVariate.of(UniformDistribution.unit(), 100, 3));
      assertTrue(tensor.length() < 100);
    }
  }

  public void testQuantity() {
    for (LineDistances lineDistances : LineDistances.values()) {
      CurveDecimation curveDecimation = CurveDecimation.of( //
          lineDistances.supply(RnManifold.INSTANCE), Quantity.of(0.7, "m"));
      Tensor tensor = curveDecimation.apply(RandomVariate.of(UniformDistribution.unit(), 100, 3).map(s -> Quantity.of(s, "m")));
      assertTrue(tensor.length() < 90);
    }
  }
}
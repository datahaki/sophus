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

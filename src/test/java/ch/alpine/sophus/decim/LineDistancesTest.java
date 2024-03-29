// code by jph
package ch.alpine.sophus.decim;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.lie.rn.RnGroup;
import ch.alpine.sophus.lie.se2.Se2Group;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.qty.Quantity;

class LineDistancesTest {
  @Test
  void testSimple() {
    for (LineDistances lineDistances : LineDistances.values()) {
      CurveDecimation curveDecimation = CurveDecimation.of( //
          lineDistances.supply(Se2Group.INSTANCE), RealScalar.of(0.4));
      Tensor tensor = curveDecimation.apply(RandomVariate.of(UniformDistribution.unit(), 100, 3));
      assertTrue(tensor.length() < 100);
    }
  }

  @Test
  void testQuantity() {
    for (LineDistances lineDistances : LineDistances.values()) {
      CurveDecimation curveDecimation = CurveDecimation.of( //
          lineDistances.supply(RnGroup.INSTANCE), Quantity.of(0.7, "m"));
      Tensor tensor = curveDecimation.apply(RandomVariate.of(UniformDistribution.unit(), 100, 3).map(s -> Quantity.of(s, "m")));
      assertTrue(tensor.length() < 90);
    }
  }
}

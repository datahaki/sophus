// code by jph
package ch.alpine.sophus.lie.se2;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.decim.CurveDecimation;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Dimensions;
import ch.alpine.tensor.alg.Reverse;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.UniformDistribution;

class Se2CurveDecimationTest {
  @Test
  public void testRandom() {
    CurveDecimation curveDecimation = Se2CurveDecimation.of(RealScalar.ONE);
    Tensor tensor = RandomVariate.of(UniformDistribution.of(-1, 1), 100, 3);
    Tensor result = curveDecimation.apply(tensor);
    List<Integer> list = Dimensions.of(result);
    assertTrue(list.get(0) < tensor.length());
    assertEquals(list.get(1).intValue(), 3);
  }

  @Test
  public void testSymmetric() {
    CurveDecimation curveDecimation = CurveDecimation.symmetric(Se2Group.INSTANCE, RealScalar.ONE);
    Tensor tensor = RandomVariate.of(UniformDistribution.of(-1, 1), 100, 3);
    Tensor result1 = curveDecimation.apply(tensor);
    List<Integer> list1 = Dimensions.of(result1);
    assertTrue(list1.get(0) < tensor.length());
    assertEquals(list1.get(1).intValue(), 3);
    Tensor result2 = curveDecimation.apply(Reverse.of(tensor));
    List<Integer> list2 = Dimensions.of(result2);
    assertEquals(list1, list2);
  }
}

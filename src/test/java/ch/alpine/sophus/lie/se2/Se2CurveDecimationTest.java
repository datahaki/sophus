// code by jph
package ch.alpine.sophus.lie.se2;

import java.util.List;

import ch.alpine.sophus.decim.CurveDecimation;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Dimensions;
import ch.alpine.tensor.alg.Reverse;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.UniformDistribution;
import junit.framework.TestCase;

public class Se2CurveDecimationTest extends TestCase {
  public void testRandom() {
    CurveDecimation curveDecimation = Se2CurveDecimation.of(RealScalar.ONE);
    Tensor tensor = RandomVariate.of(UniformDistribution.of(-1, 1), 100, 3);
    Tensor result = curveDecimation.apply(tensor);
    List<Integer> list = Dimensions.of(result);
    assertTrue(list.get(0) < tensor.length());
    assertEquals(list.get(1).intValue(), 3);
  }

  public void testSymmetric() {
    CurveDecimation curveDecimation = CurveDecimation.symmetric(Se2Manifold.INSTANCE, RealScalar.ONE);
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

// code by jph
package ch.alpine.sophus.lie.rn;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.decim.CurveDecimation;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Range;
import ch.alpine.tensor.alg.UnitVector;
import ch.alpine.tensor.ext.Serialization;
import ch.alpine.tensor.lie.TensorProduct;

public class RnCurveDecimationTest {
  @Test
  public void testSimple() throws ClassNotFoundException, IOException {
    CurveDecimation curveDecimation = Serialization.copy(RnCurveDecimation.of(RealScalar.ZERO));
    Tensor tensor = TensorProduct.of(Range.of(0, 3), UnitVector.of(2, 0));
    assertEquals(tensor, Tensors.fromString("{{0, 0}, {1, 0}, {2, 0}}"));
    Tensor apply = curveDecimation.apply(tensor);
    assertEquals(apply, Tensors.fromString("{{0, 0}, {2, 0}}"));
  }

  @Test
  public void testEmpty() {
    CurveDecimation curveDecimation = RnCurveDecimation.of(RealScalar.ONE);
    Tensor tensor = curveDecimation.apply(Tensors.empty());
    assertEquals(tensor, Tensors.empty());
  }

  @Test
  public void testSingle() {
    CurveDecimation curveDecimation = RnCurveDecimation.of(RealScalar.ONE);
    Tensor input = Tensors.of(Tensors.vector(1, 2, 3)).unmodifiable();
    Tensor tensor = curveDecimation.apply(input);
    assertEquals(tensor, input);
  }
}

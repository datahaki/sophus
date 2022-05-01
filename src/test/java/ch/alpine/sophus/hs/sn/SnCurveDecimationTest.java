// code by jph
package ch.alpine.sophus.hs.sn;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.decim.CurveDecimation;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.UnitVector;
import ch.alpine.tensor.ext.Serialization;
import ch.alpine.tensor.nrm.Vector2Norm;

class SnCurveDecimationTest {
  @Test
  public void testGeodesic() {
    Tensor p0 = Vector2Norm.NORMALIZE.apply(Tensors.vector(1, 0, 0));
    Tensor p1 = Vector2Norm.NORMALIZE.apply(Tensors.vector(0.5, 0.5, 0));
    Tensor p2 = Vector2Norm.NORMALIZE.apply(Tensors.vector(0, 1, 0));
    Tensor tensor = Tensors.of(p0, p1, p2);
    CurveDecimation curveDecimation = SnCurveDecimation.of(RealScalar.of(0.1));
    Tensor result = curveDecimation.apply(tensor);
    assertEquals(result, Tensors.of(UnitVector.of(3, 0), UnitVector.of(3, 1)));
  }

  @Test
  public void testTriangle() throws ClassNotFoundException, IOException {
    Tensor p0 = Vector2Norm.NORMALIZE.apply(Tensors.vector(1, 0, 0));
    Tensor p1 = Vector2Norm.NORMALIZE.apply(Tensors.vector(0.5, 0.5, 0.8));
    Tensor p2 = Vector2Norm.NORMALIZE.apply(Tensors.vector(0, 1, 0));
    Tensor tensor = Tensors.of(p0, p1, p2);
    CurveDecimation curveDecimation = Serialization.copy(SnCurveDecimation.of(RealScalar.of(0.1)));
    Tensor result = curveDecimation.apply(tensor);
    assertEquals(result, tensor);
  }
}

// code by jph
package ch.ethz.idsc.sophus.hs.sn;

import java.io.IOException;

import ch.ethz.idsc.sophus.decim.CurveDecimation;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.UnitVector;
import ch.ethz.idsc.tensor.ext.Serialization;
import ch.ethz.idsc.tensor.nrm.Vector2Norm;
import junit.framework.TestCase;

public class SnCurveDecimationTest extends TestCase {
  public void testGeodesic() {
    Tensor p0 = Vector2Norm.NORMALIZE.apply(Tensors.vector(1, 0, 0));
    Tensor p1 = Vector2Norm.NORMALIZE.apply(Tensors.vector(0.5, 0.5, 0));
    Tensor p2 = Vector2Norm.NORMALIZE.apply(Tensors.vector(0, 1, 0));
    Tensor tensor = Tensors.of(p0, p1, p2);
    CurveDecimation curveDecimation = SnCurveDecimation.of(RealScalar.of(0.1));
    Tensor result = curveDecimation.apply(tensor);
    assertEquals(result, Tensors.of(UnitVector.of(3, 0), UnitVector.of(3, 1)));
  }

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

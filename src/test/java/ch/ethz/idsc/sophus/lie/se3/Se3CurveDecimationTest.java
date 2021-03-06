// code by jph
package ch.ethz.idsc.sophus.lie.se3;

import java.io.IOException;

import ch.ethz.idsc.sophus.decim.CurveDecimation;
import ch.ethz.idsc.sophus.lie.so3.Rodrigues;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.api.ScalarTensorFunction;
import ch.ethz.idsc.tensor.ext.Serialization;
import junit.framework.TestCase;

public class Se3CurveDecimationTest extends TestCase {
  public void testSimple() throws ClassNotFoundException, IOException {
    CurveDecimation curveDecimation = Serialization.copy(Se3CurveDecimation.of(RealScalar.of(0.3)));
    Tensor p = Se3Matrix.of(Rodrigues.vectorExp(Tensors.vector(0.1, -.2, -.3)), Tensors.vector(4, 3, 7));
    // Se3GroupElement pe = new Se3GroupElement(p);
    Tensor q = Se3Matrix.of(Rodrigues.vectorExp(Tensors.vector(0.2, .3, -.1)), Tensors.vector(1, 2, 5));
    // Se3GroupElement qe = new Se3GroupElement(q);
    ScalarTensorFunction scalarTensorFunction = Se3Geodesic.INSTANCE.curve(p, q);
    Tensor m1 = scalarTensorFunction.apply(RealScalar.of(0.3));
    Tensor m2 = scalarTensorFunction.apply(RealScalar.of(0.8));
    Tensor curve = Tensors.of(p, m1, m2, q);
    Tensor tensor = curveDecimation.apply(curve);
    assertEquals(tensor.length(), 2);
    assertEquals(tensor, Tensors.of(p, q));
  }
}

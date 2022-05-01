// code by jph
package ch.alpine.sophus.lie.se3;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.decim.CurveDecimation;
import ch.alpine.sophus.lie.so3.Rodrigues;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.api.ScalarTensorFunction;
import ch.alpine.tensor.ext.Serialization;

class Se3CurveDecimationTest {
  @Test
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

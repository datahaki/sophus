// code by jph
package ch.alpine.sophus.lie.so2;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.num.Pi;

class So2GroupElementTest {
  @Test
  public void testInverse() {
    So2GroupElement so2GroupElement = new So2GroupElement(RealScalar.ONE);
    Scalar scalar = so2GroupElement.inverse().combine(RealScalar.ONE);
    assertEquals(scalar, RealScalar.ZERO);
    assertEquals(so2GroupElement.toCoordinate(), RealScalar.ONE);
  }

  @Test
  public void testAdjoint() {
    So2GroupElement so2GroupElement = new So2GroupElement(RealScalar.ONE);
    assertEquals(so2GroupElement.adjoint(Pi.VALUE), Pi.VALUE);
    assertEquals(so2GroupElement.dL(RealScalar.of(3)), RealScalar.of(3));
  }
}

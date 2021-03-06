// code by jph
package ch.ethz.idsc.sophus.lie.so2;

import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.num.Pi;
import junit.framework.TestCase;

public class So2GroupElementTest extends TestCase {
  public void testInverse() {
    So2GroupElement so2GroupElement = new So2GroupElement(RealScalar.ONE);
    Scalar scalar = so2GroupElement.inverse().combine(RealScalar.ONE);
    assertEquals(scalar, RealScalar.ZERO);
    assertEquals(so2GroupElement.toCoordinate(), RealScalar.ONE);
  }

  public void testAdjoint() {
    So2GroupElement so2GroupElement = new So2GroupElement(RealScalar.ONE);
    assertEquals(so2GroupElement.adjoint(Pi.VALUE), Pi.VALUE);
    assertEquals(so2GroupElement.dL(RealScalar.of(3)), RealScalar.of(3));
  }
}

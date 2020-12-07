package ch.ethz.idsc.sophus.lie.rn;

import ch.ethz.idsc.tensor.RationalScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.api.ScalarUnaryOperator;
import ch.ethz.idsc.tensor.qty.Quantity;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class RnTriangleAreaTest extends TestCase {
  public void testSimple2D() {
    Scalar scalar = RnTriangleArea.of(Tensors.vector(10, 0), Tensors.vector(10, 1), Tensors.vector(11, 0));
    Chop._10.requireClose(scalar, RationalScalar.HALF);
  }

  public void testSimpleUnits() {
    ScalarUnaryOperator suo = s -> Quantity.of(s, "m");
    Scalar scalar = RnTriangleArea.of(Tensors.vector(10, 0).map(suo), Tensors.vector(10, 1).map(suo), Tensors.vector(11, 0).map(suo));
    Chop._10.requireClose(scalar, Quantity.of(RationalScalar.HALF, "m^2"));
  }
}

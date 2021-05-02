package ch.alpine.sophus.lie.rn;

import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.api.ScalarUnaryOperator;
import ch.alpine.tensor.qty.Quantity;
import ch.alpine.tensor.sca.Chop;
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

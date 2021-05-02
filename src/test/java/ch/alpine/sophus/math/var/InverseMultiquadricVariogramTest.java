// code by jph
package ch.alpine.sophus.math.var;

import ch.alpine.sophus.usr.AssertFail;
import ch.alpine.tensor.ExactScalarQ;
import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.api.ScalarUnaryOperator;
import junit.framework.TestCase;

public class InverseMultiquadricVariogramTest extends TestCase {
  public void testSimple() {
    ScalarUnaryOperator scalarUnaryOperator = InverseMultiquadricVariogram.of(3);
    Scalar scalar = scalarUnaryOperator.apply(RealScalar.of(4));
    assertEquals(ExactScalarQ.require(scalar), RationalScalar.of(1, 5));
  }

  public void testNegativeFail() {
    AssertFail.of(() -> InverseMultiquadricVariogram.of(-3));
  }
}

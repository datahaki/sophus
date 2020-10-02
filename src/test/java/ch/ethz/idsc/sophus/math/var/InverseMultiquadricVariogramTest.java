// code by jph
package ch.ethz.idsc.sophus.math.var;

import ch.ethz.idsc.sophus.usr.AssertFail;
import ch.ethz.idsc.tensor.ExactScalarQ;
import ch.ethz.idsc.tensor.RationalScalar;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.sca.ScalarUnaryOperator;
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

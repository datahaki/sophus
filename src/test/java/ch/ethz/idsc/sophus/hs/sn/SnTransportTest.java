// code by jph
package ch.ethz.idsc.sophus.hs.sn;

import ch.ethz.idsc.sophus.usr.AssertFail;
import ch.ethz.idsc.tensor.alg.UnitVector;
import ch.ethz.idsc.tensor.api.TensorUnaryOperator;
import ch.ethz.idsc.tensor.mat.Tolerance;
import junit.framework.TestCase;

public class SnTransportTest extends TestCase {
  public void testSimple() {
    TensorUnaryOperator tensorUnaryOperator = SnTransport.INSTANCE.shift(UnitVector.of(3, 0), UnitVector.of(3, 1));
    Tolerance.CHOP.requireClose(tensorUnaryOperator.apply(UnitVector.of(3, 1)), UnitVector.of(3, 0).negate());
    Tolerance.CHOP.requireClose(tensorUnaryOperator.apply(UnitVector.of(3, 2)), UnitVector.of(3, 2));
  }

  public void testTangentFail() {
    TensorUnaryOperator tensorUnaryOperator = SnTransport.INSTANCE.shift(UnitVector.of(3, 0), UnitVector.of(3, 1));
    AssertFail.of(() -> tensorUnaryOperator.apply(UnitVector.of(3, 0)));
  }
}

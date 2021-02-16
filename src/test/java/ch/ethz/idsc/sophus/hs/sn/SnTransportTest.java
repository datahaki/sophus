// code by jph
package ch.ethz.idsc.sophus.hs.sn;

import ch.ethz.idsc.sophus.math.sample.RandomSample;
import ch.ethz.idsc.sophus.usr.AssertFail;
import ch.ethz.idsc.tensor.Tensor;
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

  public void testFlip() {
    Tensor p = RandomSample.of(SnRandomSample.of(3));
    Tensor q = RandomSample.of(SnRandomSample.of(3));
    SnExponential snExponential = new SnExponential(p);
    Tensor f1 = snExponential.exp(snExponential.log(q).negate());
    Tensor f2 = SnManifold.INSTANCE.flip(p, q);
    Tolerance.CHOP.requireClose(f1, f2);
  }

  public void testTangentFail() {
    TensorUnaryOperator tensorUnaryOperator = SnTransport.INSTANCE.shift(UnitVector.of(3, 0), UnitVector.of(3, 1));
    AssertFail.of(() -> tensorUnaryOperator.apply(UnitVector.of(3, 0)));
  }
}

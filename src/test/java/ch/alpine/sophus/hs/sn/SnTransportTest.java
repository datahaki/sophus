// code by jph
package ch.alpine.sophus.hs.sn;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.math.sample.RandomSample;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.UnitVector;
import ch.alpine.tensor.api.TensorUnaryOperator;
import ch.alpine.tensor.mat.Tolerance;

public class SnTransportTest {
  @Test
  public void testSimple() {
    TensorUnaryOperator tensorUnaryOperator = SnTransport.INSTANCE.shift(UnitVector.of(3, 0), UnitVector.of(3, 1));
    Tolerance.CHOP.requireClose(tensorUnaryOperator.apply(UnitVector.of(3, 1)), UnitVector.of(3, 0).negate());
    Tolerance.CHOP.requireClose(tensorUnaryOperator.apply(UnitVector.of(3, 2)), UnitVector.of(3, 2));
  }

  @Test
  public void testFlip() {
    Tensor p = RandomSample.of(SnRandomSample.of(3));
    Tensor q = RandomSample.of(SnRandomSample.of(3));
    SnExponential snExponential = new SnExponential(p);
    Tensor f1 = snExponential.exp(snExponential.log(q).negate());
    Tensor f2 = SnManifold.INSTANCE.flip(p, q);
    Tolerance.CHOP.requireClose(f1, f2);
  }

  @Test
  public void testTangentFail() {
    TensorUnaryOperator tensorUnaryOperator = SnTransport.INSTANCE.shift(UnitVector.of(3, 0), UnitVector.of(3, 1));
    assertThrows(Exception.class, () -> tensorUnaryOperator.apply(UnitVector.of(3, 0)));
  }
}

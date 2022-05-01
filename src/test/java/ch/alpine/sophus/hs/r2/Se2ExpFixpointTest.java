// code by jph
package ch.alpine.sophus.hs.r2;

import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.Optional;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.lie.se2c.Se2CoveringExponential;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Subdivide;
import ch.alpine.tensor.qty.Quantity;
import ch.alpine.tensor.sca.Chop;

class Se2ExpFixpointTest {
  @Test
  public void testSimple() {
    Tensor velocity = Tensors.fromString("{3[m*s^-1], .2[m*s^-1], 0.3[s^-1]}");
    Optional<Tensor> optional = Se2ExpFixpoint.of(velocity);
    for (Tensor _t : Subdivide.of(Quantity.of(-2.1, "s"), Quantity.of(10, "s"), 13)) {
      Se2Bijection se2Bijection = new Se2Bijection(Se2CoveringExponential.INSTANCE.exp(velocity.multiply((Scalar) _t)));
      Chop._10.requireClose(se2Bijection.forward().apply(optional.get()), optional.get());
    }
  }

  @Test
  public void testSimple2() {
    Tensor velocity = Tensors.fromString("{-3[m*s^-1], 1.2[m*s^-1], -0.3[s^-1]}");
    Optional<Tensor> optional = Se2ExpFixpoint.of(velocity);
    for (Tensor _t : Subdivide.of(Quantity.of(-5.1, "s"), Quantity.of(10, "s"), 17)) {
      Se2Bijection se2Bijection = new Se2Bijection(Se2CoveringExponential.INSTANCE.exp(velocity.multiply((Scalar) _t)));
      Chop._10.requireClose(se2Bijection.forward().apply(optional.get()), optional.get());
    }
  }

  @Test
  public void testEmpty() {
    Tensor velocity = Tensors.fromString("{-3[m*s^-1], 1.2[m*s^-1], -0[s^-1]}");
    Optional<Tensor> optional = Se2ExpFixpoint.of(velocity);
    assertFalse(optional.isPresent());
  }

  @Test
  public void testEmptyChop() {
    Tensor velocity = Tensors.fromString("{-3[m*s^-1], 1.2[m*s^-1], -0.00003[s^-1]}");
    Optional<Tensor> optional = Se2ExpFixpoint.of(velocity, Chop._03);
    assertFalse(optional.isPresent());
  }
}

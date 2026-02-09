// code by jph
package ch.alpine.sophus.lie.so2;

import java.util.Objects;

import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.ScalarUnaryOperator;

public class So2Lift implements ScalarUnaryOperator {
  /** @param vector
   * @return */
  public static Tensor of(Tensor vector) {
    return vector.maps(new So2Lift());
  }

  // ---
  private Scalar prev = null;

  @Override
  public Scalar apply(Scalar angle) {
    if (Objects.isNull(prev))
      return prev = angle;
    Scalar delta = So2.MOD.apply(angle.subtract(prev));
    return prev = prev.add(delta);
  }
}

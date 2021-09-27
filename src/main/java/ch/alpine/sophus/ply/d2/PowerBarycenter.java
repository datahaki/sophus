// code by jph
package ch.alpine.sophus.ply.d2;

import java.io.Serializable;
import java.util.function.BiFunction;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.ScalarUnaryOperator;
import ch.alpine.tensor.sca.Clip;
import ch.alpine.tensor.sca.Clips;
import ch.alpine.tensor.sca.Power;

/** Reference:
 * "Power Coordinates: A Geometric Construction of Barycentric Coordinates on Convex Polytopes"
 * Max Budninskiy, Beibei Liu, Yiying Tong, Mathieu Desbrun, 2016 */
public class PowerBarycenter implements BiFunction<Tensor, Scalar, Tensor>, Serializable {
  private static final Clip CLIP = Clips.interval(0, 2);

  /** @param exponent in the interval [0, 2]
   * @throws Exception if given exponent is outside the permitted interval */
  public static BiFunction<Tensor, Scalar, Tensor> of(Scalar exponent) {
    return new PowerBarycenter(CLIP.requireInside(exponent));
  }

  /** @param exponent in the interval [0, 2]
   * @return */
  public static BiFunction<Tensor, Scalar, Tensor> of(Number exponent) {
    return of(RealScalar.of(exponent));
  }

  // ---
  private final ScalarUnaryOperator power;

  private PowerBarycenter(Scalar exponent) {
    power = Power.function(exponent);
  }

  @Override
  public Tensor apply(Tensor dif, Scalar nrm) {
    return dif.divide(power.apply(nrm));
  }
}

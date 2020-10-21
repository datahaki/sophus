// code by jph
package ch.ethz.idsc.sophus.lie.r2;

import java.io.Serializable;
import java.util.function.BiFunction;

import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.api.ScalarUnaryOperator;
import ch.ethz.idsc.tensor.sca.Clip;
import ch.ethz.idsc.tensor.sca.Clips;
import ch.ethz.idsc.tensor.sca.Power;

/** Reference:
 * "Power Coordinates: A Geometric Construction of Barycentric Coordinates on Convex Polytopes"
 * Max Budninskiy, Beibei Liu, Yiying Tong, Mathieu Desbrun, 2016 */
public class PowerBarycenter implements BiFunction<Tensor, Scalar, Tensor>, Serializable {
  private static final long serialVersionUID = 4896170318818822809L;
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

  /***************************************************/
  private final ScalarUnaryOperator power;

  private PowerBarycenter(Scalar exponent) {
    power = Power.function(exponent);
  }

  @Override
  public Tensor apply(Tensor dif, Scalar nrm) {
    return dif.divide(power.apply(nrm));
  }
}

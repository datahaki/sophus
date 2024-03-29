// code by jph
package ch.alpine.sophus.lie.se2;

import java.io.Serializable;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Scalars;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.Throw;
import ch.alpine.tensor.qty.Quantity;
import ch.alpine.tensor.sca.tri.ArcTan;
import ch.alpine.tensor.sca.tri.Tan;

/** formula to convert steering angle to (front-)wheel angle
 * so that no friction arises in the ideal/no-slip scenario.
 * 
 * formula simplified from document by marcello and panos
 * 
 * see also
 * <a href="https://en.wikipedia.org/wiki/Ackermann_steering_geometry">Ackermann steering geometry</a> */
public class AckermannSteering implements Serializable {
  private final Scalar factor;

  /** function works with {@link Quantity}.
   * both input parameters are requires to have the same unit.
   * 
   * @param x_front non-zero distance from rear to front axis
   * @param y_offset distance from center of axis to tire */
  public AckermannSteering(Scalar x_front, Scalar y_offset) {
    if (Scalars.isZero(x_front))
      throw new Throw(x_front, y_offset);
    factor = y_offset.divide(x_front);
  }

  /** @param delta
   * @return angle for a wheel located at (x_front, y_offset) */
  public Scalar angle(Scalar delta) {
    Scalar tan = Tan.FUNCTION.apply(delta);
    return ArcTan.of(RealScalar.ONE.subtract(tan.multiply(factor)), tan);
  }

  /** @param delta
   * @return steering angle for two wheels located at +y_offset and -y_offset
   * as is the symmetric standard configuration of most vehicles */
  // implementation is redundant to angle function in order to reuse the computation of the tangent
  public Tensor pair(Scalar delta) {
    Scalar tan = Tan.FUNCTION.apply(delta);
    Scalar tan_factor = tan.multiply(factor);
    return Tensors.of( //
        ArcTan.of(RealScalar.ONE.subtract(tan_factor), tan), //
        ArcTan.of(RealScalar.ONE.add(tan_factor), tan));
  }
}

// code by jph
package ch.ethz.idsc.sophus.hs.sn;

import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.api.ScalarTensorFunction;
import ch.ethz.idsc.tensor.sca.ArcTan;

/** curve on the 2-dimensional sphere
 * 
 * https://de.wikipedia.org/wiki/Loxodrome */
public class S2Loxodrome implements ScalarTensorFunction {
  /** @param angle
   * @return */
  public static ScalarTensorFunction of(Scalar angle) {
    return new S2Loxodrome(angle);
  }

  /** @param angle
   * @return */
  public static ScalarTensorFunction of(Number angle) {
    return of(RealScalar.of(angle));
  }

  /***************************************************/
  private final Scalar angle;

  private S2Loxodrome(Scalar angle) {
    this.angle = angle;
  }

  @Override
  public Tensor apply(Scalar scalar) {
    return SnEulerAngle.of(Tensors.of(scalar, ArcTan.FUNCTION.apply(scalar.multiply(angle))));
  }
}

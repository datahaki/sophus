// code by jph
package ch.alpine.sophus.flt.ga;

import ch.alpine.sophus.api.GeodesicSpace;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.TensorUnaryOperator;

/** geodesic average between 3 points for symmetric weight mask
 * {factor/2, 1-factor, factor/2}
 * implemented in 2-steps
 * 
 * a factor of 0 results in the identity operator
 * typically the factor is in the interval [0, 1] */
public abstract class Regularization2Step implements TensorUnaryOperator {
  /** @param geodesicSpace
   * @param factor for instance 2/3 */
  public static TensorUnaryOperator cyclic(GeodesicSpace geodesicSpace, Scalar factor) {
    return new Regularization2StepCyclic(geodesicSpace, factor);
  }

  /** @param geodesicSpace
   * @param factor for instance 2/3 */
  public static TensorUnaryOperator string(GeodesicSpace geodesicSpace, Scalar factor) {
    return new Regularization2StepString(geodesicSpace, factor);
  }

  // ---
  private final GeodesicSpace geodesicSpace;
  private final Scalar factor;

  protected Regularization2Step(GeodesicSpace geodesicSpace, Scalar factor) {
    this.geodesicSpace = geodesicSpace;
    this.factor = factor;
  }

  /** @param prev
   * @param curr
   * @param next
   * @return [curr, [prev, next]_1/2]_factor */
  final Tensor average(Tensor prev, Tensor curr, Tensor next) {
    return geodesicSpace.split(curr, geodesicSpace.midpoint(prev, next), factor);
  }
}

// code by jph
package ch.ethz.idsc.sophus.krg;

import java.util.Objects;

import ch.ethz.idsc.sophus.math.NormalizeTotal;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;

/** Careful: Shepard interpolation does not reproduce linear functions.
 * (Because the inverse norms are not projected to the proper subspace.)
 * 
 * <p>Reference:
 * "Interpolation on Scattered Data in Multidimensions" in NR, 2007
 * 3.7.3 Shepard Interpolation */
/* package */ class ShepardWeighting implements TensorUnaryOperator {
  /** @param weightingInterface for example
   * PseudoDistances.SOLITARY.create(Se2CoveringManifold.INSTANCE, InversePowerVariogram.of(2))
   * @return */
  public static TensorUnaryOperator of(TensorUnaryOperator weightingInterface) {
    return new ShepardWeighting(Objects.requireNonNull(weightingInterface));
  }

  /***************************************************/
  private final TensorUnaryOperator tensorUnaryOperator;

  private ShepardWeighting(TensorUnaryOperator tensorUnaryOperator) {
    this.tensorUnaryOperator = tensorUnaryOperator;
  }

  @Override // from WeightingInterface
  public Tensor apply(Tensor point) {
    return NormalizeTotal.FUNCTION.apply(tensorUnaryOperator.apply(point));
  }
}

// code by jph
package ch.ethz.idsc.sophus.krg;

import java.io.Serializable;
import java.util.Objects;

import ch.ethz.idsc.sophus.hs.VectorLogManifold;
import ch.ethz.idsc.sophus.math.NormalizeTotal;
import ch.ethz.idsc.sophus.math.WeightingInterface;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.sca.ScalarUnaryOperator;

/** Careful: Shepard interpolation does not reproduce linear functions.
 * (Because the inverse norms are not projected to the proper subspace.)
 * 
 * <p>Reference:
 * "Interpolation on Scattered Data in Multidimensions" in NR, 2007
 * 3.7.3 Shepard Interpolation */
public class ShepardWeighting implements WeightingInterface, Serializable {
  /** @param weightingInterface for example
   * PseudoDistances.RELATIVE.create(Se2CoveringManifold.INSTANCE, InversePowerVariogram.of(2))
   * @return */
  public static WeightingInterface of(WeightingInterface weightingInterface) {
    return new ShepardWeighting(Objects.requireNonNull(weightingInterface));
  }

  /** @param vectorLogManifold
   * @param exponent
   * @return */
  public static WeightingInterface absolute(VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram) {
    return of(PseudoDistances.ABSOLUTE.create(vectorLogManifold, variogram));
  }

  /** @param vectorLogManifold
   * @param exponent
   * @return */
  public static WeightingInterface relative(VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram) {
    return of(PseudoDistances.RELATIVE.create(vectorLogManifold, variogram));
  }

  /***************************************************/
  private final WeightingInterface weightingInterface;

  private ShepardWeighting(WeightingInterface weightingInterface) {
    this.weightingInterface = weightingInterface;
  }

  @Override // from WeightingInterface
  public Tensor weights(Tensor sequence, Tensor point) {
    return NormalizeTotal.FUNCTION.apply(weightingInterface.weights(sequence, point));
  }
}

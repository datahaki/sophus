// code by jph
package ch.ethz.idsc.sophus.krg;

import java.io.Serializable;
import java.util.Objects;

import ch.ethz.idsc.sophus.hs.BiinvariantMean;
import ch.ethz.idsc.sophus.math.NormalizeTotal;
import ch.ethz.idsc.sophus.math.WeightingInterface;
import ch.ethz.idsc.tensor.Tensor;

/** Careful: Shepard interpolation does not reproduce linear functions.
 * (Because the inverse norms are not projected to the proper subspace.)
 * 
 * <p>Reference:
 * "Interpolation on Scattered Data in Multidimensions" in NR, 2007
 * 3.7.3 Shepard Interpolation */
public class ShepardInterpolation implements Serializable {
  /** @param weightingInterface for example
   * PseudoDistances.RELATIVE.create(Se2CoveringManifold.INSTANCE, InversePowerVariogram.of(2))
   * @param biinvariantMean
   * @return */
  public static ShepardInterpolation of(WeightingInterface weightingInterface, BiinvariantMean biinvariantMean) {
    return new ShepardInterpolation( //
        Objects.requireNonNull(weightingInterface), //
        Objects.requireNonNull(biinvariantMean));
  }

  /***************************************************/
  private final WeightingInterface weightingInterface;
  private final BiinvariantMean biinvariantMean;

  private ShepardInterpolation(WeightingInterface weightingInterface, BiinvariantMean biinvariantMean) {
    this.weightingInterface = weightingInterface;
    this.biinvariantMean = biinvariantMean;
  }

  /** @param sequence
   * @param values of same length as sequence
   * @param point
   * @return */
  public Tensor evaluate(Tensor sequence, Tensor values, Tensor point) {
    return biinvariantMean.mean(values, NormalizeTotal.FUNCTION.apply(weightingInterface.weights(sequence, point)));
  }
}

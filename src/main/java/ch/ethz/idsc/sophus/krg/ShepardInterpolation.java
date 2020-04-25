// code by jph
package ch.ethz.idsc.sophus.krg;

import java.io.Serializable;
import java.util.Objects;

import ch.ethz.idsc.sophus.math.NormalizeTotal;
import ch.ethz.idsc.sophus.math.WeightingInterface;
import ch.ethz.idsc.tensor.Tensor;

/** <p>Reference:
 * "Interpolation on Scattered Data in Multidimensions" in NR, 2007
 * 3.7.3 Shepard Interpolation */
public class ShepardInterpolation implements WeightingInterface, Serializable {
  /** @param weightingInterface
   * @param values
   * @return */
  public static WeightingInterface of(WeightingInterface weightingInterface, Tensor values) {
    return new ShepardInterpolation( //
        Objects.requireNonNull(weightingInterface), //
        Objects.requireNonNull(values));
  }

  /***************************************************/
  private final WeightingInterface weightingInterface;
  private final Tensor values;

  private ShepardInterpolation(WeightingInterface weightingInterface, Tensor values) {
    this.weightingInterface = weightingInterface;
    this.values = values;
  }

  @Override // from WeightingInterface
  public Tensor weights(Tensor sequence, Tensor point) {
    return NormalizeTotal.FUNCTION.apply(weightingInterface.weights(sequence, point)).dot(values);
  }
}

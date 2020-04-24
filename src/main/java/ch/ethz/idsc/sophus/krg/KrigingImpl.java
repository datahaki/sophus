// code by jph
package ch.ethz.idsc.sophus.krg;

import java.io.Serializable;

import ch.ethz.idsc.sophus.math.WeightingInterface;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;

/** Reference:
 * "3.7.4 Interpolation by Kriging"
 * "Interpolation on Scattered Data in Multidimensions" in NR, 2007 */
/* package */ class KrigingImpl implements Kriging, Serializable {
  private final WeightingInterface weightingInterface;
  private final Tensor sequence;
  private final Scalar one;
  private final Tensor weights;
  private final Tensor inverse;

  public KrigingImpl(WeightingInterface weightingInterface, Tensor sequence, Scalar one, Tensor weights, Tensor inverse) {
    this.weightingInterface = weightingInterface;
    this.sequence = sequence;
    this.one = one;
    this.weights = weights;
    this.inverse = inverse;
  }

  @Override // from Kriging
  public Tensor estimate(Tensor point) {
    return weightingInterface.weights(sequence, point).append(one).dot(weights);
  }

  @Override // from Kriging
  public Scalar variance(Tensor point) {
    Tensor y = weightingInterface.weights(sequence, point).append(one);
    return inverse.dot(y).dot(y).Get();
  }
}

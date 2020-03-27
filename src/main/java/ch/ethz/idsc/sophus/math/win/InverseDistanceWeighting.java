// code by jph
package ch.ethz.idsc.sophus.math.win;

import java.io.Serializable;
import java.util.Objects;

import ch.ethz.idsc.sophus.lie.rn.RnMetric;
import ch.ethz.idsc.sophus.math.NormalizeTotal;
import ch.ethz.idsc.sophus.math.TensorMetric;
import ch.ethz.idsc.tensor.DeterminateScalarQ;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Scalars;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.UnitVector;

/** Inverse Distance Weighting does not reproduce linear functions in general. Therefore,
 * Inverse distance weights <b>do not</b> fall in the category of generalized barycentric
 * coordinates.
 * 
 * <p>Reference:
 * "A two-dimensional interpolation function for irregularly-spaced data"
 * by Donald Shepard, 1968 */
public class InverseDistanceWeighting implements WeightingInterface, Serializable {
  /** @param tensorMetric non-null, for instance {@link RnMetric#INSTANCE}
   * @return */
  public static WeightingInterface of(TensorMetric tensorMetric) {
    return new InverseDistanceWeighting(tensorMetric);
  }

  /***************************************************/
  private final TensorMetric tensorMetric;

  private InverseDistanceWeighting(TensorMetric tensorMetric) {
    this.tensorMetric = Objects.requireNonNull(tensorMetric);
  }

  @Override // from BarycentricCoordinate
  public Tensor weights(Tensor sequence, Tensor point) {
    Tensor weights = Tensors.reserve(sequence.length());
    int count = 0;
    for (Tensor p : sequence) {
      Scalar distance = tensorMetric.distance(p, point);
      if (Scalars.isZero(distance))
        return UnitVector.of(sequence.length(), count);
      Scalar reciprocal = distance.reciprocal();
      if (!DeterminateScalarQ.of(reciprocal))
        return UnitVector.of(sequence.length(), count);
      weights.append(reciprocal);
      ++count;
    }
    return NormalizeTotal.FUNCTION.apply(weights);
  }
}

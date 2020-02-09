// code by jph
package ch.ethz.idsc.sophus.math.win;

import java.io.Serializable;
import java.util.Objects;

import ch.ethz.idsc.sophus.lie.rn.RnMetric;
import ch.ethz.idsc.sophus.math.NormalizeTotal;
import ch.ethz.idsc.sophus.math.TensorMetric;
import ch.ethz.idsc.tensor.NumberQ;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.UnitVector;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;

/** Inverse Distance Weighting does not reproduce linear functions
 * 
 * Reference:
 * "A two-dimensional interpolation function for irregularly-spaced data"
 * by Donald Shepard, 1968 */
public class InverseDistanceWeighting implements Serializable {
  private final TensorMetric tensorMetric;

  /** @param tensorMetric non-null, for instance {@link RnMetric#INSTANCE} */
  public InverseDistanceWeighting(TensorMetric tensorMetric) {
    this.tensorMetric = Objects.requireNonNull(tensorMetric);
  }

  /** @param tensor
   * @return operator that maps points to the normalized vector of inverse distances */
  public TensorUnaryOperator of(Tensor tensor) {
    return new Anonymous(Objects.requireNonNull(tensor));
  }

  private class Anonymous implements TensorUnaryOperator {
    private final Tensor tensor;

    public Anonymous(Tensor tensor) {
      this.tensor = tensor;
    }

    @Override
    public Tensor apply(Tensor q) {
      Tensor weights = Tensors.reserve(tensor.length());
      int count = 0;
      for (Tensor p : tensor) {
        Scalar reciprocal = tensorMetric.distance(p, q).reciprocal();
        if (!NumberQ.of(reciprocal))
          return UnitVector.of(tensor.length(), count);
        weights.append(reciprocal);
        ++count;
      }
      return NormalizeTotal.FUNCTION.apply(weights);
    }
  }
}

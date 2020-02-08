// code by jph
package ch.ethz.idsc.sophus.math.win;

import java.io.Serializable;
import java.util.Objects;

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

  /** @param tensorMetric non-null */
  public InverseDistanceWeighting(TensorMetric tensorMetric) {
    this.tensorMetric = Objects.requireNonNull(tensorMetric);
  }

  public TensorUnaryOperator of(Tensor tensor) {
    return new Dual(tensor);
  }

  private class Dual implements TensorUnaryOperator {
    private final Tensor tensor;

    public Dual(Tensor tensor) {
      this.tensor = Objects.requireNonNull(tensor);
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

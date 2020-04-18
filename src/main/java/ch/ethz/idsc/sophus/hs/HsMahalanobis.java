// code by jph
package ch.ethz.idsc.sophus.hs;

import java.io.Serializable;
import java.util.Objects;

import ch.ethz.idsc.sophus.math.TensorNorm;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.lie.TensorProduct;
import ch.ethz.idsc.tensor.mat.Inverse;
import ch.ethz.idsc.tensor.sca.Sqrt;

/** Reference:
 * "Exponential Barycenters of the Canonical Cartan Connection and Invariant Means on Lie Groups"
 * by Xavier Pennec, Vincent Arsigny, 2012, p. 39 */
public class HsMahalanobis implements Serializable {
  private final FlattenLogManifold flattenLogManifold;
  private final BiinvariantMean biinvariantMean;

  public HsMahalanobis(FlattenLogManifold flattenLogManifold, BiinvariantMean biinvariantMean) {
    this.flattenLogManifold = Objects.requireNonNull(flattenLogManifold);
    this.biinvariantMean = Objects.requireNonNull(biinvariantMean);
  }

  // TODO pick different class name!
  public class Norm implements TensorNorm, Serializable {
    private final FlattenLog flattenLog;
    private final Tensor sigma;
    private final Tensor inverse;

    public Norm(Tensor sequence, Tensor weights) {
      Tensor mean = biinvariantMean.mean(sequence, weights);
      flattenLog = flattenLogManifold.logAt(mean);
      sigma = weights.dot(Tensor.of(sequence.stream() //
          .map(flattenLog::flattenLog) //
          .map(vector -> TensorProduct.of(vector, vector))));
      inverse = Inverse.of(sigma);
    }

    @Override
    public Scalar norm(Tensor tensor) {
      Tensor log = flattenLog.flattenLog(tensor);
      return Sqrt.FUNCTION.apply(inverse.dot(log).dot(log).Get());
    }
  }
}

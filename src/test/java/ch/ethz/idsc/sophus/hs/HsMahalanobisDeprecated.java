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
/* package */ class HsMahalanobisDeprecated implements Serializable {
  private final VectorLogManifold vectorLogManifold;
  private final BiinvariantMean biinvariantMean;

  public HsMahalanobisDeprecated(VectorLogManifold vectorLogManifold, BiinvariantMean biinvariantMean) {
    this.vectorLogManifold = Objects.requireNonNull(vectorLogManifold);
    this.biinvariantMean = Objects.requireNonNull(biinvariantMean);
  }

  // pick different class name!
  public class Norm implements TensorNorm, Serializable {
    private final TangentSpace tangentSpace;
    private final Tensor sigma;
    private final Tensor inverse;

    public Norm(Tensor sequence, Tensor weights) {
      Tensor mean = biinvariantMean.mean(sequence, weights);
      tangentSpace = vectorLogManifold.logAt(mean);
      sigma = weights.dot(Tensor.of(sequence.stream() //
          .map(tangentSpace::vectorLog) //
          .map(vector -> TensorProduct.of(vector, vector))));
      inverse = Inverse.of(sigma);
    }

    @Override
    public Scalar norm(Tensor tensor) {
      Tensor log = tangentSpace.vectorLog(tensor);
      return Sqrt.FUNCTION.apply(inverse.dot(log).dot(log).Get());
    }
  }
}

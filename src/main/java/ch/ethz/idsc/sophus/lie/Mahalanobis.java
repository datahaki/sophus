// code by jph
package ch.ethz.idsc.sophus.lie;

import ch.ethz.idsc.sophus.hs.BiinvariantMean;
import ch.ethz.idsc.sophus.math.Exponential;
import ch.ethz.idsc.sophus.math.TensorNorm;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.lie.TensorProduct;
import ch.ethz.idsc.tensor.mat.Inverse;
import ch.ethz.idsc.tensor.sca.Sqrt;

/** Reference:
 * "Exponential Barycenters of the Canonical Cartan Connection and Invariant Means on Lie Groups"
 * by Xavier Pennec, Vincent Arsigny, 2012, p. 39 */
public class Mahalanobis {
  private final LieGroup lieGroup;
  private final Exponential lieExponential;
  private final BiinvariantMean biinvariantMean;

  public Mahalanobis(LieGroup lieGroup, Exponential lieExponential, BiinvariantMean biinvariantMean) {
    this.lieGroup = lieGroup;
    this.lieExponential = lieExponential;
    this.biinvariantMean = biinvariantMean;
  }

  public class Norm implements TensorNorm {
    private final Tensor sigma;
    private final Tensor inverse;
    private final LieGroupElement lieGroupElement;

    public Norm(Tensor sequence, Tensor weights) {
      Tensor mean = biinvariantMean.mean(sequence, weights);
      lieGroupElement = lieGroup.element(mean).inverse();
      sigma = weights.dot(Tensor.of(sequence.stream() //
          .map(lieGroupElement::combine) //
          .map(lieExponential::log) //
          .map(vector -> TensorProduct.of(vector, vector))));
      inverse = Inverse.of(sigma);
    }

    @Override
    public Scalar norm(Tensor tensor) {
      Tensor log = lieExponential.log(lieGroupElement.combine(tensor));
      return Sqrt.FUNCTION.apply(inverse.dot(log).dot(log).Get());
    }
  }
}

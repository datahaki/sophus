// code by jph
package ch.ethz.idsc.sophus.krg;

import java.io.Serializable;
import java.util.Objects;

import ch.ethz.idsc.sophus.hs.VectorLogManifold;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.lie.TensorProduct;
import ch.ethz.idsc.tensor.mat.PseudoInverse;

/** The reference suggests to use the inverse and the biinvariant mean m as reference point.
 * For our more general purposes, we employ the pseudo-inverse of the form evaluated at an
 * arbitrary point.
 * 
 * <p>Reference:
 * "Exponential Barycenters of the Canonical Cartan Connection and Invariant Means on Lie Groups"
 * by Xavier Pennec, Vincent Arsigny, 2012, p. 39 */
/* package */ class Mahalanobis implements Serializable {
  private final VectorLogManifold vectorLogManifold;

  public Mahalanobis(VectorLogManifold vectorLogManifold) {
    this.vectorLogManifold = Objects.requireNonNull(vectorLogManifold);
  }

  /** @param sequence of n anchor points
   * @param point
   * @return n x n symmetric bilinear form */
  public class Form implements Serializable {
    private final Tensor vs;

    public Form(Tensor sequence, Tensor point) {
      vs = Tensor.of(sequence.stream().map(vectorLogManifold.logAt(point)::vectorLog));
    }

    public Tensor sigma_inverse() {
      Tensor sigma = vs.stream() //
          .map(v -> TensorProduct.of(v, v)) //
          .reduce(Tensor::add) //
          .get(); // without scaling factor of 1/n
      return PseudoInverse.of(sigma);
    }

    public Tensor vs() {
      return vs;
    }
  }
}

// code by jph
package ch.ethz.idsc.sophus.krg;

import java.io.Serializable;

import ch.ethz.idsc.sophus.hs.HsLevers;
import ch.ethz.idsc.sophus.hs.VectorLogManifold;
import ch.ethz.idsc.tensor.Integers;
import ch.ethz.idsc.tensor.RationalScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.lie.Symmetrize;
import ch.ethz.idsc.tensor.lie.TensorProduct;
import ch.ethz.idsc.tensor.mat.PseudoInverse;

/** The reference suggests to use the inverse and the biinvariant mean m as reference point.
 * For our more general purposes, we employ the pseudo-inverse of the form evaluated at an
 * arbitrary point.
 * 
 * <p>Reference:
 * "Exponential Barycenters of the Canonical Cartan Connection and Invariant Means on Lie Groups"
 * by Xavier Pennec, Vincent Arsigny, 2012, p. 39 */
public class Mahalanobis implements Serializable {
  private final HsLevers hsLevers;

  public Mahalanobis(VectorLogManifold vectorLogManifold) {
    hsLevers = new HsLevers(vectorLogManifold);
  }

  /** @param sequence of n anchor points
   * @param point
   * @return n x n symmetric bilinear form */
  public class Form implements Serializable {
    private final Tensor levers;

    /** @param sequence of n anchor points
     * @param point */
    public Form(Tensor sequence, Tensor point) {
      Integers.requirePositive(sequence.length());
      levers = hsLevers.levers(sequence, point);
    }

    /** @return symmetric positive semidefinite matrix */
    public Tensor sigma_inverse() {
      Tensor sigma = levers.stream() //
          .map(v -> TensorProduct.of(v, v)) //
          .reduce(Tensor::add) //
          .get() //
          .multiply(RationalScalar.of(1, levers.length()));
      // computation of pseudo inverse only may result in numerical deviation from true symmetric result
      return Symmetrize.of(PseudoInverse.of(sigma));
    }

    public Tensor levers() {
      return levers;
    }
  }
}

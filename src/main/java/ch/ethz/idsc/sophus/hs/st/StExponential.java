// code by jph
package ch.ethz.idsc.sophus.hs.st;

import java.io.Serializable;

import ch.ethz.idsc.sophus.math.Exponential;
import ch.ethz.idsc.tensor.Tensor;

/** In the literature, the Stiefel manifold is denoted either as
 * St(n,p), or V_k(R^n)
 * 
 * Reference:
 * "A matrix-algebraic algorithm for the Riemannian logarithm on the Stiefel manifold
 * under the canonical metric"
 * by Ralf Zimmermann, 2017
 * https://arxiv.org/pdf/1604.05054.pdf
 * 
 * "Eichfeldtheorie"
 * by Helga Baum, 2005 */
public class StExponential implements Exponential, Serializable {
  @SuppressWarnings("unused")
  private final Tensor x;

  /** @param x column-orthogonal rectangular matrix with dimensions n x p */
  public StExponential(Tensor x) {
    this.x = StMemberQ.INSTANCE.require(x);
  }

  @Override
  public Tensor exp(Tensor v) {
    return null;
  }

  @Override
  public Tensor log(Tensor y) {
    return null;
  }

  @Override
  public Tensor vectorLog(Tensor y) {
    return null;
  }
}

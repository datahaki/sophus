// code by jph
package ch.ethz.idsc.sophus.hs.st;

import java.io.Serializable;

import ch.ethz.idsc.sophus.hs.TangentSpace;
import ch.ethz.idsc.sophus.math.Exponential;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.mat.OrthogonalMatrixQ;

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
public class StExponential implements Exponential, TangentSpace, Serializable {
  @SuppressWarnings("unused")
  private final Tensor x;

  /** @param x column-orthogonal rectangular matrix with dimensions n x p */
  public StExponential(Tensor x) {
    this.x = OrthogonalMatrixQ.require(x);
  }

  @Override
  public Tensor exp(Tensor v) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Tensor log(Tensor y) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Tensor vectorLog(Tensor y) {
    // TODO Auto-generated method stub
    return null;
  }
}

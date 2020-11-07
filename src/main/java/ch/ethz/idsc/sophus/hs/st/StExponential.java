// code by jph
package ch.ethz.idsc.sophus.hs.st;

import java.io.Serializable;

import ch.ethz.idsc.sophus.hs.HsMemberQ;
import ch.ethz.idsc.sophus.hs.TangentSpace;
import ch.ethz.idsc.sophus.math.Exponential;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.mat.Tolerance;

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
// TODO class contains unimplemented methods
public class StExponential implements Exponential, TangentSpace, Serializable {
  private static final long serialVersionUID = -2223040776214785230L;
  private static final HsMemberQ HS_MEMBER_Q = StMemberQ.of(Tolerance.CHOP);
  // ---
  @SuppressWarnings("unused")
  private final Tensor x;

  /** @param x column-orthogonal rectangular matrix with dimensions n x p */
  public StExponential(Tensor x) {
    this.x = HS_MEMBER_Q.requirePoint(x);
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

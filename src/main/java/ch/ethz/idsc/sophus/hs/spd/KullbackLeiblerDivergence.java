// code by jph
package ch.ethz.idsc.sophus.hs.spd;

import ch.ethz.idsc.tensor.RationalScalar;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.mat.Det;
import ch.ethz.idsc.tensor.mat.LinearSolve;
import ch.ethz.idsc.tensor.mat.SymmetricMatrixQ;
import ch.ethz.idsc.tensor.red.Trace;
import ch.ethz.idsc.tensor.sca.Log;

/** Reference:
 * "Covariance clustering on Riemannian manifolds for acoustic model compression"
 * by Yusuke Shinohara, Takashi Masuko, and Masami Akamine, 2010 */
public enum KullbackLeiblerDivergence {
  ;
  /** @param p
   * @param q
   * @return squared */
  public static Scalar between(Tensor p, Tensor q) {
    SymmetricMatrixQ.require(p);
    SymmetricMatrixQ.require(q);
    Tensor qinvp = LinearSolve.of(q, p);
    Scalar n = RealScalar.of(p.length());
    return Trace.of(qinvp).subtract(n).subtract(Log.FUNCTION.apply(Det.of(qinvp).abs())) //
        .multiply(RationalScalar.HALF);
  }
}

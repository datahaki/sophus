// code by ob
package ch.ethz.idsc.sophus.lie.dt;

import ch.ethz.idsc.sophus.math.Exponential;
import ch.ethz.idsc.sophus.math.sca.Expc;
import ch.ethz.idsc.sophus.math.sca.Logc;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.Flatten;
import ch.ethz.idsc.tensor.sca.Exp;
import ch.ethz.idsc.tensor.sca.Log;
import ch.ethz.idsc.tensor.sca.Sign;

/** Regarding log function:
 * If log(g) == {dl, dt}
 * then log(inv(g)) == {-dl, -dt}
 * 
 * Reference 1:
 * "Exponential Barycenters of the Canonical Cartan Connection and Invariant Means on Lie Groups"
 * by Xavier Pennec, Vincent Arsigny, pp. 25-28, 2012
 * 
 * Reference 2:
 * "Bi-invariant Means in Lie Groups. Application to Left-invariant Polyaffine Transformations."
 * by Vincent Arsigny, Xavier Pennec, Nicholas Ayache, pp. 27-31, 2006 */
public enum DtExponential implements Exponential {
  INSTANCE;

  @Override // from Exponential
  public Tensor exp(Tensor dlambda_dt) {
    Scalar dl = dlambda_dt.Get(0);
    return Tensors.of( //
        Exp.FUNCTION.apply(dl), //
        dlambda_dt.get(1).multiply(Expc.FUNCTION.apply(dl)));
  }

  @Override // from Exponential
  public Tensor log(Tensor lambda_t) {
    Scalar lambda = Sign.requirePositive(lambda_t.Get(0));
    Scalar log_l = Log.FUNCTION.apply(lambda);
    return Tensors.of( //
        log_l, //
        /* there is a typo in Reference 1 (!) */
        lambda_t.get(1).multiply(Logc.FUNCTION.apply(lambda)));
  }

  @Override // from TangentSpace
  public Tensor vectorLog(Tensor lambda_t) {
    return Flatten.of(log(lambda_t));
  }
}

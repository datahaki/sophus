// code by ob
package ch.alpine.sophus.lie.dt;

import ch.alpine.sophus.api.Exponential;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Flatten;
import ch.alpine.tensor.sca.Sign;
import ch.alpine.tensor.sca.exp.Exp;
import ch.alpine.tensor.sca.exp.Expc;
import ch.alpine.tensor.sca.exp.Log;
import ch.alpine.tensor.sca.exp.Logc;

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

// code by ob
package ch.alpine.sophus.lie.dt;

import ch.alpine.sophus.bm.BiinvariantMean;
import ch.alpine.sophus.lie.LieGroup;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Flatten;
import ch.alpine.tensor.sca.Chop;
import ch.alpine.tensor.sca.Sign;
import ch.alpine.tensor.sca.exp.Exp;
import ch.alpine.tensor.sca.exp.Expc;
import ch.alpine.tensor.sca.exp.Log;
import ch.alpine.tensor.sca.exp.Logc;

/** (n+1)-dimensional Dilation and Translations group
 * 
 * "Exponential Barycenters of the Canonical Cartan Connection and Invariant Means on Lie Groups"
 * by Xavier Pennec, Vincent Arsigny, p.25, Section 4.1:
 * "The group of scalings and translations in n-D. The study of this (quite) simple group
 * is relevant in the context of this work, because it is one of the most simple cases of
 * non-compact and non-commutative Lie groups which does not possess any bi-invariant Rie-
 * mannian metric. This group has many of the properties of rigid-body or affine
 * transformations, but with only n + 1 degrees of freedom, which simplifies greatly the
 * computations, and allows a direct 2D geometric visualization in the plane for n = 1.
 * For these reasons, this is a highly pedagogical case."
 * 
 * Regarding log function:
 * If log(g) == {dl, dt}
 * then log(inv(g)) == {-dl, -dt}
 * 
 * Reference 1:
 * "Exponential Barycenters of the Canonical Cartan Connection and Invariant Means on Lie Groups"
 * by Xavier Pennec, Vincent Arsigny, pp. 25-28, 2012
 * 
 * Reference 2:
 * "Bi-invariant Means in Lie Groups. Application to Left-invariant Polyaffine Transformations."
 * by Vincent Arsigny, Xavier Pennec, Nicholas Ayache, pp. 27-31, 2006
 * 
 * References:
 * "Exponential Barycenters of the Canonical Cartan Connection and Invariant Means on Lie Groups"
 * by Xavier Pennec, Vincent Arsigny, p.26, Section 4.1, Eq. (14).
 * 
 * Another reference for ST is "Deep Compositing Using Lie Algebras" by Tom Duff. The article
 * parameterizes the group differently with the scaling coefficient alpha := 1 - lambda */
public enum DtGroup implements LieGroup {
  INSTANCE;

  @Override // from LieGroup
  public DtGroupElement element(Tensor lambda_t) {
    return new DtGroupElement(lambda_t);
  }

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

  @Override // from Exponential
  public Tensor vectorLog(Tensor lambda_t) {
    return Flatten.of(log(lambda_t));
  }

  @Override
  public BiinvariantMean biinvariantMean(Chop chop) {
    return DtBiinvariantMean.INSTANCE;
  }
}

// code by ob
package ch.alpine.sophus.lie.td;

import java.io.Serializable;

import ch.alpine.sophus.bm.BiinvariantMean;
import ch.alpine.sophus.hs.Exponential;
import ch.alpine.sophus.lie.LieGroup;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Append;
import ch.alpine.tensor.alg.Drop;
import ch.alpine.tensor.alg.Last;
import ch.alpine.tensor.alg.VectorQ;
import ch.alpine.tensor.chq.MemberQ;
import ch.alpine.tensor.sca.Sign;
import ch.alpine.tensor.sca.exp.Exp;
import ch.alpine.tensor.sca.exp.Expc;
import ch.alpine.tensor.sca.exp.Log;
import ch.alpine.tensor.sca.exp.Logc;

/** (n+1)-dimensional Translations and Dilation group
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
 * If log(g) == {dt, dl}
 * then log(inv(g)) == {-dt, -dl}
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
 * parameterizes the group differently with the scaling coefficient alpha := 1 - lambda
 * 
 * element of (n + 1)-dimensional Scaling and Translations group
 * 
 * <p>the neutral element is {1, {0, 0, ..., 0}}
 * 
 * <p>Reference:
 * Bi-invariant Means in Lie Groups. Application to Left-invariant Polyaffine Transformations.
 * by Vincent Arsigny, Xavier Pennec, Nicholas Ayache, pp. 27-31, 2006
 * 
 * "Exponential Barycenters of the Canonical Cartan Connection and Invariant Means on Lie Groups"
 * by Xavier Pennec, Vincent Arsigny, p.27, Section 4.1, 2012:
 * 
 * @param sequence of (lambda_i, t_i) points in ST(n) and weights non-negative and normalized
 * @return associated biinvariant mean which is the solution to the barycentric equation
 * 
 * Reference 1:
 * "Exponential Barycenters of the Canonical Cartan Connection and Invariant Means on Lie Groups"
 * by Xavier Pennec, Vincent Arsigny, p.26, Section 4.1, 2012:
 * "ST (n) is one of the most simple non-compact and non-commutative Lie groups. As expected for
 * such Lie groups, it has no bi-invariant metric."
 * 
 * Reference 2:
 * "Bi-invariant Means in Lie Groups. Application to Left-invariant Polyaffine Transformations."
 * by Vincent Arsigny, Xavier Pennec, Nicholas Ayache, p.29, 2006 */
public class TdGroup implements LieGroup, Serializable {
  public static final TdGroup INSTANCE = new TdGroup();

  @Override
  public MemberQ isPointQ() {
    return t_lambda -> VectorQ.of(t_lambda) //
        && Sign.isPositive(Last.of(t_lambda));
  }

  private enum Exponential0 implements Exponential {
    INSTANCE;

    @Override // from Exponential
    public Tensor exp(Tensor dt_dlambda) {
      Scalar dl = Last.of(dt_dlambda);
      return Append.of( //
          Drop.tail(dt_dlambda, 1).multiply(Expc.FUNCTION.apply(dl)), //
          Exp.FUNCTION.apply(dl));
    }

    @Override // from Exponential
    public Tensor log(Tensor t_lambda) {
      Scalar lambda = Sign.requirePositive(Last.of(t_lambda));
      Scalar log_l = Log.FUNCTION.apply(lambda);
      return Append.of( //
          /* there is a typo in Reference 1 (!) */
          Drop.tail(t_lambda, 1).multiply(Logc.FUNCTION.apply(lambda)), //
          log_l);
    }

    @Override
    public MemberQ isTangentQ() {
      return VectorQ::of;
    }
  }

  @Override
  public final Exponential exponential0() {
    return Exponential0.INSTANCE;
  }

  @Override
  public BiinvariantMean biinvariantMean() {
    return TdBiinvariantMean.INSTANCE;
  }

  @Override
  public final Tensor neutral(Tensor element) {
    return Append.of(Drop.tail(element, 1).maps(Scalar::zero), Last.of(element).maps(Scalar::one));
  }

  @Override
  public final Tensor invert(Tensor t_lambda) {
    Tensor t = Drop.tail(t_lambda, 1);
    Scalar lambda = Last.of(t_lambda);
    return Append.of(t.divide(lambda.negate()), lambda.reciprocal());
  }

  @Override // from LieGroupElement
  public final Tensor combine(Tensor element1, Tensor element2) {
    Tensor t1 = Drop.tail(element1, 1);
    Scalar lambda1 = Last.of(element1);
    Tensor t2 = Drop.tail(element2, 1);
    Scalar lambda2 = Last.of(element2);
    return Append.of( //
        t1.multiply(lambda2).add(t2), //
        Sign.requirePositive(lambda1.multiply(lambda2)));
  }

  @Override // from LieGroupElement
  public final Tensor adjoint(Tensor t_lambda, Tensor dt_dlambda) {
    Tensor t = Drop.tail(t_lambda, 1);
    Scalar lambda = Sign.requirePositive(Last.of(t_lambda));
    Tensor dt = Drop.tail(dt_dlambda, 1);
    Scalar dlambda = Last.of(dt_dlambda);
    return Append.of(t.multiply(dlambda).add(dt).divide(lambda), dlambda);
  }

  @Override // from LieGroupElement
  public Tensor dL(Tensor t_lambda, Tensor tensor) {
    throw new UnsupportedOperationException();
  }

  @Override
  public String toString() {
    return "Td";
  }
}

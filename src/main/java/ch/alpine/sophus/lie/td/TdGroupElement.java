// code by ob
package ch.alpine.sophus.lie.td;

import java.io.Serializable;

import ch.alpine.sophus.lie.LieGroupElement;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Append;
import ch.alpine.tensor.alg.Drop;
import ch.alpine.tensor.alg.Last;
import ch.alpine.tensor.sca.Sign;

/** element of (n + 1)-dimensional Scaling and Translations group
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
 * @see TdGroup */
public class TdGroupElement implements LieGroupElement, Serializable {
  private final Tensor t;
  private final Scalar lambda;

  /** @param lambda_t of the form {lambda, t}
   * @throws Exception if lambda is not strictly positive */
  public TdGroupElement(Tensor t_lambda) {
    this(Drop.tail(t_lambda, 1), Last.of(t_lambda));
  }

  private TdGroupElement(Tensor t, Scalar lambda) {
    this.t = t;
    this.lambda = Sign.requirePositive(lambda);
  }

  @Override // from LieGroupElement
  public Tensor toCoordinate() {
    return Append.of(t, lambda);
  }

  public Scalar lambda() {
    return lambda;
  }

  public Tensor t() {
    return t.unmodifiable();
  }

  @Override // from LieGroupElement
  public TdGroupElement inverse() {
    return new TdGroupElement( //
        t.divide(lambda.negate()), //
        lambda.reciprocal());
  }

  @Override // from LieGroupElement
  public Tensor combine(Tensor t_lambda) {
    TdGroupElement tdGroupElement = new TdGroupElement(t_lambda);
    return Append.of( //
        tdGroupElement.t.multiply(lambda).add(t), //
        tdGroupElement.lambda.multiply(lambda));
  }

  @Override // from LieGroupElement
  public Tensor adjoint(Tensor dt_dlambda) {
    Scalar dlambda = Last.of(dt_dlambda);
    return Append.of( //
        Drop.tail(dt_dlambda, 1).multiply(lambda).subtract(t.multiply(dlambda)), //
        dlambda);
  }

  @Override // from LieGroupElement
  public Tensor dL(Tensor tensor) {
    throw new UnsupportedOperationException();
  }
}

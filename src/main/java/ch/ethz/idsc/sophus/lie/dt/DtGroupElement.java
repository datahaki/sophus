// code by ob
package ch.ethz.idsc.sophus.lie.dt;

import java.io.Serializable;

import ch.ethz.idsc.sophus.lie.LieGroupElement;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.sca.Sign;

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
 * @see DtGroup */
public class DtGroupElement implements LieGroupElement, Serializable {
  private static final long serialVersionUID = 6208657139932851834L;
  // ---
  private final Scalar lambda;
  private final Tensor t;

  /** @param lambda_t of the form {lambda, t}
   * @throws Exception if lambda is not strictly positive */
  public DtGroupElement(Tensor lambda_t) {
    this(Sign.requirePositive(lambda_t.Get(0)), lambda_t.get(1));
  }

  private DtGroupElement(Scalar lambda, Tensor t) {
    this.lambda = lambda;
    this.t = t;
  }

  @Override // from LieGroupElement
  public Tensor toCoordinate() {
    return Tensors.of(lambda, t);
  }

  public Scalar lambda() {
    return lambda;
  }

  public Tensor t() {
    return t.unmodifiable();
  }

  @Override // from LieGroupElement
  public DtGroupElement inverse() {
    return new DtGroupElement( //
        lambda.reciprocal(), //
        t.divide(lambda.negate()));
  }

  @Override // from LieGroupElement
  public Tensor combine(Tensor lambda_t) {
    DtGroupElement stGroupElement = new DtGroupElement(lambda_t);
    return Tensors.of( //
        stGroupElement.lambda.multiply(lambda), //
        stGroupElement.t.multiply(lambda).add(t));
  }

  @Override // from LieGroupElement
  public Tensor adjoint(Tensor dlambda_dt) {
    Scalar dlambda = dlambda_dt.Get(0);
    return Tensors.of( //
        dlambda, //
        dlambda_dt.get(1).multiply(lambda).subtract(t.multiply(dlambda)));
  }

  @Override // from LieGroupElement
  public Tensor dL(Tensor tensor) {
    throw new UnsupportedOperationException();
  }
}

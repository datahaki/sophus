// code by jph
package ch.alpine.sophus.lie;

import java.io.Serializable;

import ch.alpine.sophus.api.Exponential;
import ch.alpine.tensor.Tensor;

/** all tangent vectors are assumed to be in the tangent space at the neutral element,
 * i.e. given in the basis of TeG */
/* package */ final class LieExponential implements Exponential, Serializable {
  private final Exponential exponential;
  private final LieGroupElement element;
  private final LieGroupElement inverse;

  public LieExponential(LieGroup lieGroup, Tensor p) {
    exponential = lieGroup.exponential();
    element = lieGroup.element(p);
    inverse = element.inverse();
  }

  @Override // from Exponential
  public Tensor exp(Tensor v) {
    return element.combine(exponential.exp(v));
  }

  @Override // from Exponential
  public Tensor log(Tensor q) {
    return exponential.log(inverse.combine(q));
  }

  @Override // from TangentSpace
  public Tensor vectorLog(Tensor q) {
    return exponential.vectorLog(inverse.combine(q));
  }
}

// code by jph
package ch.alpine.sophus.lie.r2s;

import ch.alpine.sophus.api.Exponential;
import ch.alpine.sophus.lie.LieGroup;
import ch.alpine.sophus.lie.LieGroupElement;
import ch.alpine.sophus.lie.he.HeGroup;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.ScalarTensorFunction;

/** @see HeGroup */
public enum R2SGroup implements LieGroup {
  INSTANCE;

  @Override // from LieGroup
  public LieGroupElement element(Tensor xyu) {
    return new R2SGroupElement(xyu);
  }

  @Override
  public Exponential exponential() {
    return R2SExponential.INSTANCE;
  }

  @Override // from TensorGeodesic
  public ScalarTensorFunction curve(Tensor p, Tensor q) {
    R2SGroupElement p_act = new R2SGroupElement(p);
    Tensor delta = p_act.inverse().combine(q);
    Tensor x = R2SExponential.INSTANCE.log(delta);
    return scalar -> p_act.combine(R2SExponential.INSTANCE.exp(x.multiply(scalar)));
  }
}

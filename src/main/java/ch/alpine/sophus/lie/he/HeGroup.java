// code by jph
package ch.alpine.sophus.lie.he;

import ch.alpine.sophus.api.Exponential;
import ch.alpine.sophus.lie.LieGroup;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.ScalarTensorFunction;

/** (2*n+1)-dimensional Heisenberg group */
public enum HeGroup implements LieGroup {
  INSTANCE;

  @Override // from LieGroup
  public HeGroupElement element(Tensor xyz) {
    return new HeGroupElement(xyz);
  }

  @Override
  public Exponential exponential() {
    return HeExponential.INSTANCE;
  }

  @Override // from Geodesic
  public ScalarTensorFunction curve(Tensor p, Tensor q) {
    // TODO SOPHUS probably this impl does not add value
    HeGroupElement p_act = new HeGroupElement(p);
    Tensor delta = p_act.inverse().combine(q);
    Tensor x = HeExponential.INSTANCE.log(delta);
    return scalar -> p_act.combine(HeExponential.INSTANCE.exp(x.multiply(scalar)));
  }
}

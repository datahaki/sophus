// code by jph
package ch.alpine.sophus.lie.he;

import ch.alpine.sophus.lie.LieGroup;
import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Flatten;
import ch.alpine.tensor.api.ScalarTensorFunction;

/** (2*n+1)-dimensional Heisenberg group */
public enum HeGroup implements LieGroup {
  INSTANCE;

  @Override // from LieGroup
  public HeGroupElement element(Tensor xyz) {
    return new HeGroupElement(xyz);
  }

  @Override // from Exponential
  public Tensor exp(Tensor uvw) {
    Tensor u = uvw.get(0);
    Tensor v = uvw.get(1);
    Scalar w = uvw.Get(2);
    return Tensors.of( //
        u, //
        v, //
        w.add(u.dot(v).multiply(RationalScalar.HALF)));
  }

  @Override // from Exponential
  public Tensor log(Tensor xyz) {
    Tensor x = xyz.get(0);
    Tensor y = xyz.get(1);
    Scalar z = xyz.Get(2);
    return Tensors.of( //
        x, //
        y, //
        z.subtract(x.dot(y).multiply(RationalScalar.HALF)));
  }

  @Override // from TangentSpace
  public Tensor vectorLog(Tensor xyz) {
    return Flatten.of(log(xyz));
  }

  @Override // from Geodesic
  public ScalarTensorFunction curve(Tensor p, Tensor q) {
    // TODO SOPHUS probably this impl does not add value
    HeGroupElement p_act = new HeGroupElement(p);
    Tensor delta = p_act.inverse().combine(q);
    Tensor x = log(delta);
    return scalar -> p_act.combine(exp(x.multiply(scalar)));
  }
}

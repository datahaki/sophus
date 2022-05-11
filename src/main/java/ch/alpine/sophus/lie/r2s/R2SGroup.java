// code by jph
package ch.alpine.sophus.lie.r2s;

import ch.alpine.sophus.bm.BiinvariantMean;
import ch.alpine.sophus.lie.LieGroup;
import ch.alpine.sophus.lie.LieGroupElement;
import ch.alpine.sophus.lie.he.HeGroup;
import ch.alpine.sophus.lie.so2.So2;
import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Flatten;
import ch.alpine.tensor.api.ScalarTensorFunction;
import ch.alpine.tensor.sca.Chop;

/** @see HeGroup */
public enum R2SGroup implements LieGroup {
  INSTANCE;

  @Override // from LieGroup
  public LieGroupElement element(Tensor xyu) {
    return new R2SGroupElement(xyu);
  }

  @Override // from Exponential
  public Tensor exp(Tensor uvw) {
    Scalar u = uvw.Get(0);
    Scalar v = uvw.Get(1);
    Scalar w = uvw.Get(2);
    return Tensors.of( //
        u, //
        v, //
        So2.MOD.apply(w.add(u.multiply(v).multiply(RationalScalar.HALF))));
  }

  @Override // from Exponential
  public Tensor log(Tensor xyu) {
    Scalar x = xyu.Get(0);
    Scalar y = xyu.Get(1);
    Scalar z = So2.MOD.apply(xyu.Get(2));
    return Tensors.of( //
        x, //
        y, //
        z.subtract(x.multiply(y).multiply(RationalScalar.HALF)));
  }

  @Override // from Exponential
  public Tensor vectorLog(Tensor xyz) {
    return Flatten.of(log(xyz));
  }

  @Override // from TensorGeodesic
  public ScalarTensorFunction curve(Tensor p, Tensor q) {
    R2SGroupElement p_act = new R2SGroupElement(p);
    Tensor delta = p_act.inverse().combine(q);
    Tensor x = log(delta);
    return scalar -> p_act.combine(exp(x.multiply(scalar)));
  }

  @Override
  public BiinvariantMean biinvariantMean(Chop chop) {
    throw new UnsupportedOperationException();
  }
}

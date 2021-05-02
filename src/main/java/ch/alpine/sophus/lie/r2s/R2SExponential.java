// code by jph
package ch.alpine.sophus.lie.r2s;

import ch.alpine.sophus.lie.so2.So2;
import ch.alpine.sophus.math.Exponential;
import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Flatten;

public enum R2SExponential implements Exponential {
  INSTANCE;

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

  @Override // from TangentSpace
  public Tensor vectorLog(Tensor xyz) {
    return Flatten.of(log(xyz));
  }
}

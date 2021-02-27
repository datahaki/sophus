// code by jph
package ch.ethz.idsc.sophus.lie.r2s;

import ch.ethz.idsc.sophus.lie.so2.So2;
import ch.ethz.idsc.sophus.math.Exponential;
import ch.ethz.idsc.tensor.RationalScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.Flatten;

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

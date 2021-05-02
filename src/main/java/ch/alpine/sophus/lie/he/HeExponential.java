// code by jph
package ch.alpine.sophus.lie.he;

import ch.alpine.sophus.math.Exponential;
import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Flatten;

public enum HeExponential implements Exponential {
  INSTANCE;

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
}

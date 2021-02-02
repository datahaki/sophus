// code by jph
package ch.ethz.idsc.sophus.lie.he;

import ch.ethz.idsc.sophus.hs.TangentSpace;
import ch.ethz.idsc.sophus.math.Exponential;
import ch.ethz.idsc.tensor.RationalScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.Flatten;

public enum HeExponential implements Exponential, TangentSpace {
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

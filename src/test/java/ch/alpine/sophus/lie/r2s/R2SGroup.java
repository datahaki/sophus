// code by jph
package ch.alpine.sophus.lie.r2s;

import ch.alpine.sophus.bm.BiinvariantMean;
import ch.alpine.sophus.hs.Exponential;
import ch.alpine.sophus.lie.LieGroup;
import ch.alpine.sophus.lie.he.HeGroup;
import ch.alpine.sophus.lie.so2.So2;
import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Array;
import ch.alpine.tensor.alg.VectorQ;

/** @see HeGroup */
public enum R2SGroup implements LieGroup {
  INSTANCE;

  @Override
  public boolean isMember(Tensor xyu) {
    return VectorQ.ofLength(xyu, 3);
  }

  @Override
  public Tensor neutral(Tensor element) {
    return Array.zeros(3);
  }

  public enum Exponential0 implements Exponential {
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
  }

  @Override
  public Exponential exponential0() {
    return Exponential0.INSTANCE;
  }

  @Override
  public BiinvariantMean biinvariantMean() {
    throw new UnsupportedOperationException();
  }

  @Override
  public Tensor invert(Tensor xyu) {
    Scalar x = xyu.Get(0);
    Scalar y = xyu.Get(1);
    Scalar u = So2.MOD.apply(xyu.Get(2));
    return Tensors.of( //
        x.negate(), //
        y.negate(), //
        So2.MOD.apply(x.multiply(y).subtract(u)));
  }

  @Override
  public Tensor combine(Tensor vector, Tensor xyu) {
    Scalar x = vector.Get(0);
    Scalar y = vector.Get(1);
    Scalar u = So2.MOD.apply(vector.Get(2));
    Scalar x2 = xyu.Get(0);
    Scalar y2 = xyu.Get(1);
    Scalar u2 = xyu.Get(2);
    return Tensors.of( //
        x.add(x2), //
        y.add(y2), //
        So2.MOD.apply(u.add(u2).add(x.multiply(y2))));
  }

  @Override
  public Tensor adjoint(Tensor point, Tensor tensor) {
    throw new UnsupportedOperationException();
  }

  @Override
  public Tensor dL(Tensor point, Tensor tensor) {
    throw new UnsupportedOperationException();
  }
}

// code by jph
package ch.alpine.sophus.crv.clt.mid;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Scalars;
import ch.alpine.tensor.api.ScalarBinaryOperator;
import ch.alpine.tensor.num.Pi;

/* package */ enum MidpointTangentLayers implements ScalarBinaryOperator {
  INSTANCE;

  @Override
  public Scalar apply(Scalar s1, Scalar s2) {
    if (Scalars.lessThan(s2, Pi.VALUE))
      return MidpointTangentOrder4.INSTANCE.apply(s1, s2);
    if (Scalars.lessThan(s2, RealScalar.of(5)))
      return MidpointTangentLayer1.INSTANCE.apply(s1, s2);
    return MidpointTangentLayer2.INSTANCE.apply(s1, s2);
  }
}

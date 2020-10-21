// code by jph
package ch.ethz.idsc.sophus.clt.mid;

import java.util.function.BinaryOperator;

import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Scalars;
import ch.ethz.idsc.tensor.num.Pi;

/* package */ enum MidpointTangentLayers implements BinaryOperator<Scalar> {
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

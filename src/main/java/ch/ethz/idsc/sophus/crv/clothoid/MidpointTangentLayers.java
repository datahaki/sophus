// code by jph
package ch.ethz.idsc.sophus.crv.clothoid;

import ch.ethz.idsc.sophus.math.ScalarBinaryOperator;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Scalars;
import ch.ethz.idsc.tensor.opt.Pi;

public enum MidpointTangentLayers implements ScalarBinaryOperator {
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

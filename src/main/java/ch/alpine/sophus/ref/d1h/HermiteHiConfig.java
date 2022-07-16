// code by jph
package ch.alpine.sophus.ref.d1h;

import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.Scalar;

public record HermiteHiConfig(Scalar theta, Scalar omega) {
  public static final HermiteHiConfig STANDARD = new HermiteHiConfig( //
      RationalScalar.of(+1, 128), //
      RationalScalar.of(-1, 16));
}

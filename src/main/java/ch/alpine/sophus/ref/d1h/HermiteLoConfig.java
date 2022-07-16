// code by jph
package ch.alpine.sophus.ref.d1h;

import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.Scalar;

public record HermiteLoConfig(Scalar lambda, Scalar mu) {
  public static final HermiteLoConfig STANDARD = new HermiteLoConfig( //
      RationalScalar.of(-1, 8), //
      RationalScalar.of(-1, 2));
  public static final HermiteLoConfig MANIFOLD = new HermiteLoConfig( //
      RationalScalar.of(-1, 5), //
      RationalScalar.of(9, 10));
}

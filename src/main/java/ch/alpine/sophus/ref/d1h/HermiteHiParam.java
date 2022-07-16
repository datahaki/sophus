// code by jph
package ch.alpine.sophus.ref.d1h;

import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.Scalar;

public class HermiteHiParam {
  /* package */ static final HermiteHiParam STANDARD = new HermiteHiParam( //
      RationalScalar.of(+1, 128), //
      RationalScalar.of(-1, 16));
  // ---
  public Scalar theta;
  public Scalar omega;

  public HermiteHiParam(Scalar theta, Scalar omega) {
    this.theta = theta;
    this.omega = omega;
  }
}

// code by jph
package ch.ethz.idsc.sophus.hs.hn;

import java.io.Serializable;

import ch.ethz.idsc.sophus.math.sca.SinhcInverse;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Scalars;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.sca.ArcCosh;
import ch.ethz.idsc.tensor.sca.Chop;

public class HnAngle implements Serializable {
  private final Tensor x;
  private final Tensor y;
  private final Scalar cosh_d;

  public HnAngle(Tensor x, Tensor y) {
    this.x = x;
    this.y = y;
    cosh_d = cosh_d(x, y);
  }

  public Scalar cosh_d() {
    return cosh_d;
  }

  public Scalar angle() {
    return ArcCosh.FUNCTION.apply(cosh_d);
  }

  // TODO the presence of this function here hides that log is log_x(y)
  public Tensor log() {
    return y.subtract(x.multiply(cosh_d())).multiply(SinhcInverse.FUNCTION.apply(angle()));
  }

  /** @param x
   * @param y
   * @return result guaranteed to be greater equals 1 */
  private static Scalar cosh_d(Tensor x, Tensor y) {
    HnMemberQ.INSTANCE.require(x);
    HnMemberQ.INSTANCE.require(y);
    Scalar xy = HnBilinearForm.between(x, y).negate();
    if (Scalars.lessEquals(RealScalar.ONE, xy))
      return xy;
    // TODO use taylor series
    Chop._08.requireClose(xy, RealScalar.ONE);
    return RealScalar.ONE;
  }
}

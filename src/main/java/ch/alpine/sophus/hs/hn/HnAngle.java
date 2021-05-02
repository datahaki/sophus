// code by jph
package ch.alpine.sophus.hs.hn;

import ch.alpine.sophus.math.sca.SinhcInverse;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Scalars;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.TensorScalarFunction;
import ch.alpine.tensor.sca.ArcCosh;
import ch.alpine.tensor.sca.Chop;

public class HnAngle implements TensorScalarFunction {
  private final Tensor x;

  /** @param x */
  public HnAngle(Tensor x) {
    this.x = HnMemberQ.INSTANCE.require(x);
  }

  /** @param y
   * @return result guaranteed to be greater equals 1 */
  private Scalar _cosh_d(Tensor y) {
    Scalar cosh_d = LBilinearForm.between(x, y).negate();
    if (Scalars.lessEquals(RealScalar.ONE, cosh_d))
      return cosh_d;
    // TODO use taylor series
    Chop._08.requireClose(cosh_d, RealScalar.ONE);
    return RealScalar.ONE;
  }

  @Override
  public Scalar apply(Tensor y) {
    return new Inner(y, _cosh_d(y)).angle();
  }

  public Tensor log(Tensor y) {
    return new Inner(y, _cosh_d(y)).log();
  }

  /***************************************************/
  private class Inner {
    private final Tensor y;
    private final Scalar cosh_d;
    private final Scalar angle;

    private Inner(Tensor y, Scalar cosh_d) {
      this.y = HnMemberQ.INSTANCE.require(y);
      this.cosh_d = cosh_d;
      angle = ArcCosh.FUNCTION.apply(cosh_d);
    }

    public Scalar angle() {
      return angle;
    }

    public Tensor log() {
      return y.subtract(x.multiply(cosh_d)).multiply(SinhcInverse.FUNCTION.apply(angle));
    }
  }
}

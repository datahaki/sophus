// code by jph
package ch.ethz.idsc.sophus.krg;

import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.pdf.BinningMethod;
import ch.ethz.idsc.tensor.sca.Exp;
import ch.ethz.idsc.tensor.sca.ScalarUnaryOperator;
import ch.ethz.idsc.tensor.sca.Sign;

/** Reference:
 * "Radial Basis Functions in General Use", eq (3.7.8)
 * in NR, 2007
 * 
 * @see BinningMethod */
public class GaussianRadialBasisFunction implements ScalarUnaryOperator {
  /** @param r0 non-negative */
  public static ScalarUnaryOperator of(Scalar r0) {
    return new GaussianRadialBasisFunction(Sign.requirePositiveOrZero(r0));
  }

  /***************************************************/
  private final Scalar r0;

  private GaussianRadialBasisFunction(Scalar r0) {
    this.r0 = r0;
  }

  @Override // from TensorNorm
  public Scalar apply(Scalar r) {
    Scalar factor = r.divide(r0);
    return Exp.FUNCTION.apply(factor.multiply(factor).negate());
  }
}

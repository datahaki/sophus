// code by jph
package ch.alpine.sophus.math.var;

import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.api.ScalarUnaryOperator;
import ch.alpine.tensor.pdf.BinningMethod;
import ch.alpine.tensor.sca.Exp;
import ch.alpine.tensor.sca.Sign;

/** Reference:
 * "Radial Basis Functions in General Use", eq (3.7.8)
 * in NR, 2007
 * 
 * <p>The input of the variogram has unit of r0.
 * The output of the variogram is unitless.
 * 
 * @see BinningMethod */
public class GaussianVariogram implements ScalarUnaryOperator {
  /** @param r0 positive */
  public static ScalarUnaryOperator of(Scalar r0) {
    return new GaussianVariogram(Sign.requirePositive(r0));
  }

  // ---
  private final Scalar r0;

  private GaussianVariogram(Scalar r0) {
    this.r0 = r0;
  }

  @Override
  public Scalar apply(Scalar r) {
    Scalar factor = r.divide(r0);
    return Exp.FUNCTION.apply(factor.multiply(factor).negate());
  }

  @Override // from Object
  public String toString() {
    return String.format("%s[%s]", getClass().getSimpleName(), r0);
  }
}

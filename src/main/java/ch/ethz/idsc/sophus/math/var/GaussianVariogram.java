// code by jph
package ch.ethz.idsc.sophus.math.var;

import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.pdf.BinningMethod;
import ch.ethz.idsc.tensor.sca.Exp;
import ch.ethz.idsc.tensor.sca.ScalarUnaryOperator;
import ch.ethz.idsc.tensor.sca.Sign;

/** Reference:
 * "Radial Basis Functions in General Use", eq (3.7.8)
 * in NR, 2007
 * 
 * <p>The input of the variogram has unit of r0.
 * The output of the variogram is unitless.
 * 
 * @see BinningMethod */
public class GaussianVariogram implements ScalarUnaryOperator {
  private static final long serialVersionUID = 5379873849964779943L;

  /** @param r0 non-negative */
  public static ScalarUnaryOperator of(Scalar r0) {
    return new GaussianVariogram(Sign.requirePositiveOrZero(r0));
  }

  /***************************************************/
  private final Scalar r0;

  private GaussianVariogram(Scalar r0) {
    this.r0 = r0;
  }

  @Override
  public Scalar apply(Scalar r) {
    Scalar factor = r.divide(r0);
    return Exp.FUNCTION.apply(factor.multiply(factor).negate());
  }
}

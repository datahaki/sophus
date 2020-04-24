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
  /** -2*r0*r0 */
  private final Scalar n2r0;

  private GaussianRadialBasisFunction(Scalar r0) {
    Scalar r0_squared = r0.multiply(r0);
    n2r0 = r0_squared.add(r0_squared).negate();
  }

  @Override // from TensorNorm
  public Scalar apply(Scalar r) {
    return Exp.FUNCTION.apply(r.multiply(r).divide(n2r0));
  }
}

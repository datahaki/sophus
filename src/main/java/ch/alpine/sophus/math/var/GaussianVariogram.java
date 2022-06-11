// code by jph
package ch.alpine.sophus.math.var;

import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.api.ScalarUnaryOperator;
import ch.alpine.tensor.pdf.BinningMethod;
import ch.alpine.tensor.sca.Sign;
import ch.alpine.tensor.sca.exp.Exp;

/** Reference:
 * "Radial Basis Functions in General Use", eq (3.7.8)
 * in NR, 2007
 * 
 * <p>The input of the variogram has unit of r0.
 * The output of the variogram is unitless.
 * 
 * @see BinningMethod
 * 
 * @param r0 positive */
public record GaussianVariogram(Scalar r0) implements ScalarUnaryOperator {
  public GaussianVariogram {
    Sign.requirePositive(r0);
  }

  @Override
  public Scalar apply(Scalar r) {
    Scalar factor = r.divide(r0);
    return Exp.FUNCTION.apply(factor.multiply(factor).negate());
  }

  @Override // from Object
  public String toString() {
    return String.format("GaussianVariogram[%s]", r0);
  }
}

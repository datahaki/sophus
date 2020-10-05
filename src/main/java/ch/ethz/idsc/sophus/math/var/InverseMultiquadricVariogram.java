// code by jph
package ch.ethz.idsc.sophus.math.var;

import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.pdf.BinningMethod;
import ch.ethz.idsc.tensor.sca.ScalarUnaryOperator;
import ch.ethz.idsc.tensor.sca.Sign;

/** Reference:
 * "Radial Basis Functions in General Use", eq (3.7.6)
 * in NR, 2007
 * 
 * @see BinningMethod */
public class InverseMultiquadricVariogram extends MultiquadricVariogram {
  private static final long serialVersionUID = 2932263743728525502L;

  /** @param r0 non-negative */
  public static ScalarUnaryOperator of(Scalar r0) {
    return new InverseMultiquadricVariogram(Sign.requirePositiveOrZero(r0));
  }

  /** @param r0 non-negative */
  public static ScalarUnaryOperator of(Number r0) {
    return of(RealScalar.of(r0));
  }

  /***************************************************/
  private InverseMultiquadricVariogram(Scalar r0) {
    super(r0);
  }

  @Override // from TensorNorm
  public Scalar apply(Scalar r) {
    return super.apply(r).reciprocal();
  }
}

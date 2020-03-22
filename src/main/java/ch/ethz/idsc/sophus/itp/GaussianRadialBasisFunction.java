// code by jph
package ch.ethz.idsc.sophus.itp;

import java.io.Serializable;

import ch.ethz.idsc.sophus.math.TensorNorm;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.pdf.BinningMethod;
import ch.ethz.idsc.tensor.red.Norm2Squared;
import ch.ethz.idsc.tensor.sca.Exp;
import ch.ethz.idsc.tensor.sca.Sign;

/** Reference:
 * "Radial Basis Functions in General Use", eq (3.7.8)
 * in NR, 2007
 * 
 * @see BinningMethod */
public class GaussianRadialBasisFunction implements TensorNorm, Serializable {
  /** -2*r0*r0 */
  private final Scalar n2r0;

  /** @param r0 non-negative */
  public GaussianRadialBasisFunction(Scalar r0) {
    Scalar r0_squared = Sign.requirePositiveOrZero(r0).multiply(r0);
    n2r0 = r0_squared.add(r0_squared).negate();
  }

  @Override // from TensorNorm
  public Scalar norm(Tensor tensor) {
    return Exp.FUNCTION.apply(Norm2Squared.ofVector(tensor).divide(n2r0));
  }
}

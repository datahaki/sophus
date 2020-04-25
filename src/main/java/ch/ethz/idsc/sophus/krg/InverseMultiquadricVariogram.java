// code by jph
package ch.ethz.idsc.sophus.krg;

import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.pdf.BinningMethod;

/** Reference:
 * "Radial Basis Functions in General Use", eq (3.7.6)
 * in NR, 2007
 * 
 * @see BinningMethod */
public class InverseMultiquadricVariogram extends MultiquadricVariogram {
  /** @param r0 non-negative */
  public InverseMultiquadricVariogram(Scalar r0) {
    super(r0);
  }

  @Override // from TensorNorm
  public Scalar apply(Scalar r) {
    return super.apply(r).reciprocal();
  }
}

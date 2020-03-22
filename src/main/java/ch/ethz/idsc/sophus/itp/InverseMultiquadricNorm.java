// code by jph
package ch.ethz.idsc.sophus.itp;

import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.pdf.BinningMethod;

/** Reference:
 * "Radial Basis Functions in General Use", eq (3.7.6)
 * in NR, 2007
 * 
 * @see BinningMethod */
public class InverseMultiquadricNorm extends MultiquadricNorm {
  /** @param r0 non-negative */
  public InverseMultiquadricNorm(Scalar r0) {
    super(r0);
  }

  @Override // from TensorNorm
  public Scalar norm(Tensor vector) {
    return super.norm(vector).reciprocal();
  }
}
// code by jph
package ch.ethz.idsc.sophus.hs;

import java.io.Serializable;
import java.util.Objects;

import ch.ethz.idsc.tensor.Tensor;

/** Any m from the group that satisfies the equation below is refers to as
 * the biinvariant mean to the given sequence of points x_i and weights w_i.
 * 
 * <pre>
 * 0 == sum_i w_i * log (m^-1).x_i
 * </pre>
 * 
 * <p>For given parameters, the defect is refered to as the right hand side
 * of the equation and may be non-zero.
 * 
 * <p>Reference:
 * "Exponential Barycenters of the Canonical Cartan Connection and Invariant Means on Lie Groups"
 * by Xavier Pennec, Vincent Arsigny, p.21, 2012
 * 
 * "Generalized Barycentric Coordinates in Computer Graphics and Computational Mechanics"
 * by Kai Hormann, N. Sukumar, Eq. 1.11, 2017 */
public class BiinvariantMeanDefect implements MeanDefect, Serializable {
  /** @param hsExponential non-null
   * @return */
  public static MeanDefect of(HsExponential hsExponential) {
    return new BiinvariantMeanDefect(Objects.requireNonNull(hsExponential));
  }

  /***************************************************/
  private final HsExponential hsExponential;

  private BiinvariantMeanDefect(HsExponential hsExponential) {
    this.hsExponential = hsExponential;
  }

  @Override // from MeanDefect
  public Tensor defect(Tensor sequence, Tensor weights, Tensor mean) {
    return weights.dot(Tensor.of(sequence.stream().map(hsExponential.exponential(mean)::log)));
  }
}

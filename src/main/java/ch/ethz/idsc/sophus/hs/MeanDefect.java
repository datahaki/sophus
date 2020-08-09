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
public class MeanDefect implements Serializable {
  private final HsExponential hsExponential;

  /** @param hsExponential non-null */
  public MeanDefect(HsExponential hsExponential) {
    this.hsExponential = Objects.requireNonNull(hsExponential);
  }

  /** output is subject to exponentiation
   * 
   * @param sequence
   * @param weights
   * @param mean for weighted mean of given sequence
   * @return vector in the direction of true mean */
  public Tensor defect(Tensor sequence, Tensor weights, Tensor mean) {
    return weights.dot(Tensor.of(sequence.stream().map(hsExponential.exponential(mean)::log)));
  }
}

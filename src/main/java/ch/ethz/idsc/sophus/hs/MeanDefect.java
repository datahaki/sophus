// code by jph
package ch.ethz.idsc.sophus.hs;

import java.io.Serializable;

import ch.ethz.idsc.sophus.math.Exponential;
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
 * by Kai Hormann, N. Sukumar, Eq. 1.11, 2017
 * 
 * @see BiinvariantMean */
public class MeanDefect implements Serializable {
  private final Exponential exponential;
  private final Tensor tangent;

  /** output is subject to exponentiation
   * 
   * @param sequence
   * @param weights
   * @param exponential at estimated weighted average of given sequence */
  public MeanDefect(Tensor sequence, Tensor weights, Exponential exponential) {
    this.exponential = exponential;
    tangent = weights.dot(Tensor.of(sequence.stream().map(exponential::log)));
  }

  /** @return tangent vector in the direction of true weighted average
   * (however, vector may have the form of a matrix) */
  public Tensor tangent() {
    return tangent;
  }

  /** @return better guess of weighted average on manifold */
  public Tensor shifted() {
    return exponential.exp(tangent);
  }
}

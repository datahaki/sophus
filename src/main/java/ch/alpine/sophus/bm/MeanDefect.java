// code by jph
package ch.alpine.sophus.bm;

import java.io.Serializable;

import ch.alpine.sophus.hs.Exponential;
import ch.alpine.tensor.Tensor;

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
 * tangent vector in the direction of true weighted average.
 * Careful: tangent vector may have the form of a matrix. */
public record MeanDefect(Exponential exponential, Tensor tangent) implements Serializable {
  /** output is subject to exponentiation
   * 
   * @param sequence
   * @param weights
   * @param exponential at estimated weighted average of given sequence */
  public static MeanDefect of(Tensor sequence, Tensor weights, Exponential exponential) {
    return new MeanDefect(exponential, weights.dot(exponential.log().slash(sequence)));
  }

  /** @return better guess of weighted average on manifold */
  public Tensor shifted() {
    return exponential.exp(tangent);
  }
}

// code by jph
package ch.ethz.idsc.sophus.lie;

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
 * by Hormann, Sukumar, Eq. 1.11, 2016 */
public class BiinvariantMeanDefect implements Serializable {
  private final LieGroup lieGroup;
  private final LieExponential lieExponential;

  /** @param lieGroup
   * @param lieExponential */
  public BiinvariantMeanDefect(LieGroup lieGroup, LieExponential lieExponential) {
    this.lieGroup = Objects.requireNonNull(lieGroup);
    this.lieExponential = Objects.requireNonNull(lieExponential);
  }

  /** @param sequence of elements in Lie group
   * @param weights
   * @param mean
   * @return vector of the lie algebra, zero if given mean is the weighted mean of the given sequence */
  public Tensor evaluate(Tensor sequence, Tensor weights, Tensor mean) {
    return weights.dot(Tensor.of(sequence.stream() //
        .map(lieGroup.element(mean).inverse()::combine) //
        .map(lieExponential::log)));
  }
}

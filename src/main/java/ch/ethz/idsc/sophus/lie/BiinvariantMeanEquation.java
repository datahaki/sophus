// code by jph
package ch.ethz.idsc.sophus.lie;

import java.io.Serializable;
import java.util.Objects;

import ch.ethz.idsc.tensor.Tensor;

/** 0 == sum_i w_i * log (m^-1).x_i
 * 
 * Reference:
 * "Exponential Barycenters of the Canonical Cartan Connection and Invariant Means on Lie Groups"
 * by Xavier Pennec, Vincent Arsigny, 2012
 * 
 * see also Eq. 1.11
 * "Generalized Barycentric Coordinates in Computer Graphics and Computational Mechanics"
 * by Hormann Sukumar */
public class BiinvariantMeanEquation implements Serializable {
  private final LieGroup lieGroup;
  private final LieExponential lieExponential;

  /** @param lieGroup
   * @param lieExponential */
  public BiinvariantMeanEquation(LieGroup lieGroup, LieExponential lieExponential) {
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

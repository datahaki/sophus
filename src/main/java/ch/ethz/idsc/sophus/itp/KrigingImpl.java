// code by jph
package ch.ethz.idsc.sophus.itp;

import java.io.Serializable;

import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;

/* package */ class KrigingImpl implements Kriging, Serializable {
  private final PseudoDistances pseudoDistances;
  private final Scalar one;
  private final Tensor weights;
  private final Tensor inverse;

  public KrigingImpl(PseudoDistances pseudoDistances, Scalar one, Tensor weights, Tensor inverse) {
    this.pseudoDistances = pseudoDistances;
    this.one = one;
    this.weights = weights;
    this.inverse = inverse;
  }

  @Override // from Kriging
  public Tensor estimate(Tensor point) {
    return pseudoDistances.pseudoDistances(point).append(one).dot(weights);
  }

  @Override // from Kriging
  public Scalar variance(Tensor point) {
    Tensor y = pseudoDistances.pseudoDistances(point).append(one);
    return inverse.dot(y).dot(y).Get();
  }
}
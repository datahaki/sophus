// code by jph
package ch.alpine.sophus.lie.so;

import ch.alpine.sophus.bm.MeanEstimate;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.mat.pd.Orthogonalize;

/** created and tested by jph, 2021.FEB.20
 * 
 * formula is exact for midpoint computation, i.e. where
 * sequence == {p, q} and weights == {1/2, 1/2} */
public enum SoPhongMean implements MeanEstimate {
  INSTANCE;

  @Override // from BiinvariantMean
  public Tensor estimate(Tensor sequence, Tensor weights) {
    return Orthogonalize.usingSvd(weights.dot(sequence));
  }
}

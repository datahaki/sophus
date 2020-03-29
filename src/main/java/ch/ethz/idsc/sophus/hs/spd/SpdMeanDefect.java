// code by jph
package ch.ethz.idsc.sophus.hs.spd;

import ch.ethz.idsc.sophus.hs.MeanDefect;
import ch.ethz.idsc.tensor.Tensor;

public enum SpdMeanDefect implements MeanDefect {
  INSTANCE;

  @Override // from MeanDefect
  public Tensor defect(Tensor sequence, Tensor weights, Tensor mean) {
    return weights.dot(Tensor.of(sequence.stream().map(new SpdExponential(mean)::log)));
  }
}

// code by jph
package ch.ethz.idsc.sophus.hs.spd;

import ch.ethz.idsc.sophus.hs.BiinvariantMean;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.TensorRuntimeException;
import ch.ethz.idsc.tensor.red.ArgMax;
import ch.ethz.idsc.tensor.red.Frobenius;
import ch.ethz.idsc.tensor.sca.Chop;

public enum SpdMean implements BiinvariantMean {
  INSTANCE;

  private static final int MAX_ITERATIONS = 100;
  private static final Chop CHOP = Chop._10;

  @Override // from BiinvariantMean
  public Tensor mean(Tensor sequence, Tensor weights) {
    Tensor mean = sequence.get(ArgMax.of(weights));
    int count = 0;
    while (++count < MAX_ITERATIONS) {
      Tensor log = SpdMeanDefect.INSTANCE.defect(sequence, weights, mean); // sim matrix
      if (CHOP.allZero(Frobenius.NORM.ofMatrix(log)))
        return mean;
      mean = new SpdExponential(mean).exp(log);
    }
    throw TensorRuntimeException.of(sequence, weights);
  }
}

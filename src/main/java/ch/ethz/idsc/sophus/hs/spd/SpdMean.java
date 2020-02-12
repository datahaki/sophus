// code by jph
package ch.ethz.idsc.sophus.hs.spd;

import ch.ethz.idsc.sophus.lie.BiinvariantMean;
import ch.ethz.idsc.sophus.lie.MeanDefect;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.TensorRuntimeException;
import ch.ethz.idsc.tensor.red.ArgMax;
import ch.ethz.idsc.tensor.red.Frobenius;
import ch.ethz.idsc.tensor.sca.Chop;

public enum SpdMean implements BiinvariantMean, MeanDefect {
  INSTANCE;

  private static final int MAX_ITERATIONS = 100;
  private static final Chop CHOP = Chop._10;

  @Override // from BiinvariantMean
  public Tensor mean(Tensor sequence, Tensor weights) {
    Tensor mean = sequence.get(ArgMax.of(weights));
    int count = 0;
    while (++count < MAX_ITERATIONS) {
      Tensor log = defect(sequence, weights, mean); // sim matrix
      if (CHOP.allZero(Frobenius.NORM.ofMatrix(log)))
        return mean;
      mean = new SpdExp(mean).exp(log);
    }
    throw TensorRuntimeException.of(sequence, weights);
  }

  /** @param sequence
   * @param weights
   * @param candidate
   * @return matrix in Sim */
  @Override
  public Tensor defect(Tensor sequence, Tensor weights, Tensor candidate) {
    return weights.dot(Tensor.of(sequence.stream().map(new SpdExp(candidate)::log)));
  }
}

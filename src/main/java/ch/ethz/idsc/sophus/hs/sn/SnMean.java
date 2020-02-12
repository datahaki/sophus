// code by jph
package ch.ethz.idsc.sophus.hs.sn;

import ch.ethz.idsc.sophus.lie.BiinvariantMean;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.TensorRuntimeException;
import ch.ethz.idsc.tensor.alg.Normalize;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;
import ch.ethz.idsc.tensor.red.ArgMax;
import ch.ethz.idsc.tensor.red.Norm;
import ch.ethz.idsc.tensor.sca.Chop;

public enum SnMean implements BiinvariantMean {
  INSTANCE;

  private static final int MAX_ITERATIONS = 100;
  private static final Chop CHOP = Chop._14;
  private static final TensorUnaryOperator NORMALIZE = Normalize.with(Norm._2);

  @Override
  public Tensor mean(Tensor sequence, Tensor weights) {
    Tensor mean = sequence.get(ArgMax.of(weights));
    int count = 0;
    while (++count < MAX_ITERATIONS) {
      Tensor log = SnMean.defect(sequence, weights, mean);
      if (CHOP.allZero(Norm._2.ofVector(log)))
        return mean;
      mean = new SnExp(mean).exp(log);
      // normalization for numerical stability
      mean = NORMALIZE.apply(mean);
    }
    throw TensorRuntimeException.of(sequence, weights);
  }

  public static Tensor defect(Tensor sequence, Tensor weights, Tensor mean) {
    return weights.dot(Tensor.of(sequence.stream().map(new SnExp(mean)::log)));
  }
}

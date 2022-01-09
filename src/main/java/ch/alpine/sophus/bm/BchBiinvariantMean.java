// code by jph
package ch.alpine.sophus.bm;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.BinaryOperator;

import ch.alpine.sophus.lie.rn.RnBiinvariantMean;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Fold;

/** sequence is given in tangent space */
public class BchBiinvariantMean implements BiinvariantMean, Serializable {
  public static BiinvariantMean of(BinaryOperator<Tensor> bch) {
    return new BchBiinvariantMean(Objects.requireNonNull(bch));
  }

  // ---
  private final BinaryOperator<Tensor> bch;

  /** @param bch non-null */
  private BchBiinvariantMean(BinaryOperator<Tensor> bch) {
    this.bch = bch;
  }

  @Override
  public Tensor mean(Tensor sequence, Tensor weights) {
    Tensor queue = Tensors.empty();
    // ---
    for (int c = 0; c < 10; ++c) {
      Tensor jump = RnBiinvariantMean.INSTANCE.mean(sequence, weights);
      queue.append(jump);
      Tensor points = Tensors.empty();
      for (Tensor p : sequence)
        points.append(bch.apply(jump.negate(), p));
      sequence = points;
    }
    return Fold.of(bch, queue.get(0).map(Scalar::zero), queue);
  }
}

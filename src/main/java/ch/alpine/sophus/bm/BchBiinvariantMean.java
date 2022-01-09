// code by jph
package ch.alpine.sophus.bm;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.BinaryOperator;

import ch.alpine.sophus.lie.rn.RnBiinvariantMean;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.TensorRuntimeException;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Fold;
import ch.alpine.tensor.nrm.Vector2Norm;
import ch.alpine.tensor.sca.Chop;

/** sequence is given in tangent space */
public class BchBiinvariantMean implements BiinvariantMean, Serializable {
  private static final int MAX_ITERATIONS = 100;

  public static BiinvariantMean of(BinaryOperator<Tensor> bch, Chop chop) {
    return new BchBiinvariantMean( //
        Objects.requireNonNull(bch), //
        Objects.requireNonNull(chop));
  }

  // ---
  private final BinaryOperator<Tensor> bch;
  private final Chop chop;

  /** @param bch non-null */
  private BchBiinvariantMean(BinaryOperator<Tensor> bch, Chop chop) {
    this.bch = bch;
    this.chop = chop;
  }

  @Override
  public Tensor mean(Tensor sequence, Tensor weights) {
    // TODO building is queue is not strictly required
    Tensor queue = Tensors.empty();
    // ---
    for (int count = 0; count < MAX_ITERATIONS; ++count) {
      Tensor defect = RnBiinvariantMean.INSTANCE.mean(sequence, weights);
      queue.append(defect);
      if (chop.isZero(Vector2Norm.of(defect)))
        return Fold.of(bch, queue.get(0).map(Scalar::zero), queue);
      Tensor inv = defect.negate();
      sequence = Tensor.of(sequence.stream().map(point -> bch.apply(inv, point)));
    }
    throw TensorRuntimeException.of(sequence, weights);
  }
}

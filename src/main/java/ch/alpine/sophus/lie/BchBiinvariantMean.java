// code by jph
package ch.alpine.sophus.lie;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.BinaryOperator;

import ch.alpine.sophus.bm.BiinvariantMean;
import ch.alpine.sophus.lie.rn.RnBiinvariantMean;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.TensorRuntimeException;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.nrm.Vector2Norm;
import ch.alpine.tensor.sca.Chop;

/** sequence is given in tangent space */
public class BchBiinvariantMean implements BiinvariantMean, Serializable {
  private static final int MAX_ITERATIONS = 100;

  /** @param bch non-null
   * @param chop non-null
   * @return */
  public static BiinvariantMean of(BinaryOperator<Tensor> bch, Chop chop) {
    return new BchBiinvariantMean( //
        Objects.requireNonNull(bch), //
        Objects.requireNonNull(chop));
  }

  public static BiinvariantMean of(BinaryOperator<Tensor> bch) {
    return of(bch, Tolerance.CHOP);
  }

  // ---
  private final BinaryOperator<Tensor> bch;
  private final Chop chop;

  private BchBiinvariantMean(BinaryOperator<Tensor> bch, Chop chop) {
    this.bch = bch;
    this.chop = chop;
  }

  @Override // from BiinvariantMean
  public Tensor mean(final Tensor sequence, Tensor weights) {
    Tensor _sequence = sequence;
    Tensor prev = sequence.get(0).map(Scalar::zero);
    for (int count = 0; count < MAX_ITERATIONS; ++count) {
      Tensor next = mean_negate(_sequence, weights, prev);
      if (chop.isClose(prev, next))
        return next.negate();
      _sequence = Tensor.of(sequence.stream().map(point -> bch.apply(next, point)));
      prev = next;
    }
    throw TensorRuntimeException.of(sequence, weights);
  }

  private Tensor mean_negate(Tensor sequence, Tensor weights, Tensor mean_negate) {
    for (int count = 0; count < MAX_ITERATIONS; ++count) {
      Tensor defect = RnBiinvariantMean.INSTANCE.mean(sequence, weights).negate();
      if (chop.isZero(Vector2Norm.of(defect)))
        return mean_negate;
      mean_negate = bch.apply(defect, mean_negate);
      sequence = Tensor.of(sequence.stream().map(point -> bch.apply(defect, point)));
    }
    throw TensorRuntimeException.of(sequence, weights);
  }
}

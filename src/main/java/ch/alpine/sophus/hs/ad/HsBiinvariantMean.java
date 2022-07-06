// code by jph
package ch.alpine.sophus.hs.ad;

import java.io.Serializable;
import java.util.Objects;

import ch.alpine.sophus.bm.BiinvariantMean;
import ch.alpine.sophus.lie.rn.RnBiinvariantMean;
import ch.alpine.sophus.math.AffineQ;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Throw;
import ch.alpine.tensor.alg.Array;
import ch.alpine.tensor.api.TensorUnaryOperator;
import ch.alpine.tensor.sca.Chop;

public class HsBiinvariantMean implements BiinvariantMean, Serializable {
  private static final int MAX_ITERATIONS = 15;

  /** @param hsAlgebra non-null
   * @param chop non-null
   * @return */
  public static BiinvariantMean of(HsAlgebra hsAlgebra, Chop chop) {
    return new HsBiinvariantMean( //
        Objects.requireNonNull(hsAlgebra), //
        Objects.requireNonNull(chop));
  }

  /** @param hsAlgebra non-null
   * @return */
  public static BiinvariantMean of(HsAlgebra hsAlgebra) {
    return of(hsAlgebra, Chop._14);
  }

  // ---
  private final HsAlgebra hsAlgebra;
  private final Chop chop;

  private HsBiinvariantMean(HsAlgebra hsAlgebra, Chop chop) {
    this.hsAlgebra = hsAlgebra;
    this.chop = chop;
  }

  @Override // from BiinvariantMean
  public Tensor mean(final Tensor sequence, Tensor weights) {
    AffineQ.require(weights);
    Tensor _sequence = sequence;
    Tensor prev = Array.zeros(hsAlgebra.dimM());
    for (int count = 0; count < MAX_ITERATIONS; ++count) {
      Tensor next = mean_negate(_sequence, weights, prev);
      if (chop.isClose(prev, next))
        return next.negate();
      _sequence = Tensor.of(sequence.stream().map(hsAlgebra.action(hsAlgebra.lift(next))));
      prev = next;
    }
    throw Throw.of(sequence, weights);
  }

  private Tensor mean_negate(Tensor sequence, Tensor weights, Tensor mean_negate) {
    for (int count = 0; count < MAX_ITERATIONS; ++count) {
      Tensor defect = RnBiinvariantMean.INSTANCE.mean(sequence, weights).negate();
      if (chop.allZero(defect))
        return mean_negate;
      TensorUnaryOperator tuo = hsAlgebra.action(hsAlgebra.lift(defect));
      sequence = Tensor.of(sequence.stream().map(tuo));
      mean_negate = tuo.apply(mean_negate);
    }
    throw Throw.of(sequence, weights);
  }
}

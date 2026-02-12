// code by jph
package ch.alpine.sophus.hs;

import java.io.Serializable;
import java.util.Objects;

import ch.alpine.sophus.bm.BiinvariantMean;
import ch.alpine.sophus.bm.LinearBiinvariantMean;
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
    AffineQ.INSTANCE.require(weights);
    Tensor _sequence = sequence;
    Tensor prev = Array.zeros(hsAlgebra.dimM());
    for (int iteration = 0; iteration < MAX_ITERATIONS; ++iteration) {
      Tensor next = mean_negate(_sequence, weights, prev);
      if (chop.isClose(prev, next))
        return next.negate();
      _sequence = hsAlgebra.action(hsAlgebra.lift(next)).slash(sequence);
      prev = next;
    }
    throw new Throw(sequence, weights);
  }

  private Tensor mean_negate(Tensor sequence, Tensor weights, Tensor mean_negate) {
    for (int iteration = 0; iteration < MAX_ITERATIONS; ++iteration) {
      Tensor defect = LinearBiinvariantMean.INSTANCE.mean(sequence, weights).negate();
      if (chop.allZero(defect))
        return mean_negate;
      TensorUnaryOperator tuo = hsAlgebra.action(hsAlgebra.lift(defect));
      sequence = tuo.slash(sequence);
      mean_negate = tuo.apply(mean_negate);
    }
    throw new Throw(sequence, weights);
  }
}

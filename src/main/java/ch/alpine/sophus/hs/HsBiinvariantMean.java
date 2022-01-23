// code by jph
package ch.alpine.sophus.hs;

import java.io.Serializable;
import java.util.Objects;

import ch.alpine.sophus.bm.BiinvariantMean;
import ch.alpine.sophus.lie.HsAlgebra;
import ch.alpine.sophus.lie.rn.RnBiinvariantMean;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.TensorRuntimeException;
import ch.alpine.tensor.alg.Array;
import ch.alpine.tensor.api.TensorUnaryOperator;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.nrm.Vector2Norm;
import ch.alpine.tensor.sca.Chop;

public class HsBiinvariantMean implements BiinvariantMean, Serializable {
  private static final int MAX_ITERATIONS_OUTER = 10;
  private static final int MAX_ITERATIONS_INNER = 100;

  /** @param bch non-null
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
    return of(hsAlgebra, Tolerance.CHOP);
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
    Tensor _sequence = sequence;
    Tensor prev = Array.zeros(hsAlgebra.dimM());
    for (int count = 0; count < MAX_ITERATIONS_OUTER; ++count) {
      Tensor next = mean_negate(_sequence, weights, prev);
      if (chop.isClose(prev, next))
        return next.negate();
      _sequence = Tensor.of(sequence.stream().map(hsAlgebra.action(hsAlgebra.lift(next))));
      prev = next;
    }
    throw TensorRuntimeException.of(sequence, weights);
  }

  private Tensor mean_negate(Tensor sequence, Tensor weights, Tensor mean_negate) {
    for (int count = 0; count < MAX_ITERATIONS_INNER; ++count) {
      Tensor defect = RnBiinvariantMean.INSTANCE.mean(sequence, weights).negate();
      if (chop.isZero(Vector2Norm.of(defect)))
        return mean_negate;
      TensorUnaryOperator tuo = hsAlgebra.action(hsAlgebra.lift(defect));
      sequence = Tensor.of(sequence.stream().map(tuo));
      mean_negate = tuo.apply(mean_negate);
    }
    throw TensorRuntimeException.of(sequence, weights);
  }
}

// code by jph
package ch.alpine.sophus.bm;

import java.io.Serializable;
import java.util.Objects;

import ch.alpine.sophus.lie.HsAlgebra;
import ch.alpine.sophus.lie.rn.RnBiinvariantMean;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.TensorRuntimeException;
import ch.alpine.tensor.alg.Array;
import ch.alpine.tensor.nrm.Vector2Norm;
import ch.alpine.tensor.sca.Chop;

/** sequence is given in tangent space */
public class HsBiinvariantMean implements BiinvariantMean, Serializable {
  private static final int MAX_ITERATIONS = 100;

  /** @param bch non-null
   * @param chop non-null
   * @return */
  public static BiinvariantMean of(HsAlgebra hsAlgebra, Chop chop) {
    return new HsBiinvariantMean( //
        Objects.requireNonNull(hsAlgebra), //
        Objects.requireNonNull(chop));
  }

  // ---
  private final HsAlgebra hsAlgebra;
  private final Chop chop;

  private HsBiinvariantMean(HsAlgebra hsAlgebra, Chop chop) {
    this.hsAlgebra = hsAlgebra;
    this.chop = chop;
  }

  @Override // from BiinvariantMean
  public Tensor mean(Tensor sequence, Tensor weights) {
    Tensor meaninv = Array.zeros(hsAlgebra.dimM());
    meaninv = meaninv(sequence, weights, meaninv);
    return meaninv(sequence, weights, meaninv).negate();
  }

  private Tensor meaninv(Tensor sequence, Tensor weights, Tensor meaninv) {
    Tensor lift = hsAlgebra.lift(meaninv);
    sequence = Tensor.of(sequence.stream().map(point -> hsAlgebra.action(lift, point)));
    for (int count = 0; count < MAX_ITERATIONS; ++count) {
      Tensor defect = RnBiinvariantMean.INSTANCE.mean(sequence, weights).negate();
      if (chop.isZero(Vector2Norm.of(defect)))
        return meaninv;
      meaninv = hsAlgebra.action(hsAlgebra.lift(defect), meaninv);
      sequence = Tensor.of(sequence.stream().map(point -> hsAlgebra.action(hsAlgebra.lift(defect), point)));
    }
    throw TensorRuntimeException.of(sequence, weights);
  }
}

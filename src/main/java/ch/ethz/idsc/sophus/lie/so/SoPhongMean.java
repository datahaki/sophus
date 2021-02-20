// code by jph
package ch.ethz.idsc.sophus.lie.so;

import ch.ethz.idsc.sophus.bm.BiinvariantMean;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.mat.Det;
import ch.ethz.idsc.tensor.mat.Orthogonalize;
import ch.ethz.idsc.tensor.mat.Tolerance;

/** created and tested by jph */
public enum SoPhongMean implements BiinvariantMean {
  INSTANCE;

  @Override // from BiinvariantMean
  public Tensor mean(Tensor sequence, Tensor weights) {
    Tensor matrix = Orthogonalize.usingPD(weights.dot(sequence));
    Tolerance.CHOP.requireClose(Det.of(matrix), RealScalar.ONE);
    return matrix;
  }
}

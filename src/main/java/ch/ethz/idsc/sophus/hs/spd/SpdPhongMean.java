// code by jph
package ch.ethz.idsc.sophus.hs.spd;

import ch.ethz.idsc.sophus.bm.BiinvariantMean;
import ch.ethz.idsc.sophus.math.WeightedGeometricMean;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.lie.MatrixExp;
import ch.ethz.idsc.tensor.lie.MatrixLog;

/** formula is not even exact for simple case of midpoint computation
 * 
 * @see WeightedGeometricMean */
public enum SpdPhongMean implements BiinvariantMean {
  INSTANCE;

  @Override // from BiinvariantMean
  public Tensor mean(Tensor sequence, Tensor weights) {
    return MatrixExp.ofSymmetric(weights.dot(Tensor.of(sequence.stream().map(MatrixLog::ofSymmetric))));
  }
}

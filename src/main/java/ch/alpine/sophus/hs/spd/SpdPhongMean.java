// code by jph
package ch.alpine.sophus.hs.spd;

import ch.alpine.sophus.bm.BiinvariantMean;
import ch.alpine.sophus.math.WeightedGeometricMean;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.mat.ex.MatrixExp;
import ch.alpine.tensor.mat.ex.MatrixLog;

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

// code by jph
package ch.alpine.sophus.lie.so;

import ch.alpine.sophus.bm.BiinvariantMean;
import ch.alpine.sophus.lie.LieGroup;
import ch.alpine.sophus.lie.so3.So3Group;
import ch.alpine.sophus.math.LowerVectorize;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.mat.ex.MatrixExp;
import ch.alpine.tensor.mat.ex.MatrixLog;
import ch.alpine.tensor.sca.Chop;

/** special orthogonal group of n x n orthogonal matrices with determinant 1
 * 
 * SO(n) group of orthogonal matrices with determinant +1
 * 
 * for n == 3 use {@link So3Group} */
public enum SoGroup implements LieGroup {
  INSTANCE;

  @Override // from LieGroup
  public SoGroupElement element(Tensor matrix) {
    return SoGroupElement.of(matrix);
  }

  @Override // from Exponential
  public Tensor exp(Tensor matrix) {
    return MatrixExp.of(matrix);
  }

  @Override // from Exponential
  public Tensor log(Tensor matrix) {
    return MatrixLog.of(matrix);
  }

  @Override // from TangentSpace
  public Tensor vectorLog(Tensor matrix) {
    return LowerVectorize.of(log(matrix), -1);
  }

  @Override
  public BiinvariantMean biinvariantMean(Chop chop) {
    // TODO SOPHUS ALG probably not the best
    return SoPhongMean.INSTANCE;
  }
}

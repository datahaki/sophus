// code by jph
package ch.alpine.sophus.lie.gl;

import ch.alpine.sophus.bm.BiinvariantMean;
import ch.alpine.sophus.lie.LieGroup;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Flatten;
import ch.alpine.tensor.mat.ex.MatrixExp;
import ch.alpine.tensor.mat.ex.MatrixLog;
import ch.alpine.tensor.sca.Chop;

/** Lie group GL(n) of invertible square matrices
 * also called "immersely linear Lie group"
 * 
 * Exp_e[X] and Log_e[M]
 * 
 * input X is a square matrix
 * input M is an invertible matrix */
public enum GlGroup implements LieGroup {
  INSTANCE;

  @Override // from LieGroup
  public GlGroupElement element(Tensor matrix) {
    return GlGroupElement.of(matrix);
  }

  @Override // from Exponential
  public Tensor exp(Tensor matrix) {
    return MatrixExp.of(matrix);
  }

  @Override // from Exponential
  public Tensor log(Tensor matrix) {
    return MatrixLog.of(matrix);
  }

  @Override // from Exponential
  public Tensor vectorLog(Tensor matrix) {
    return Flatten.of(log(matrix));
  }

  @Override
  public BiinvariantMean biinvariantMean(Chop chop) {
    throw new UnsupportedOperationException();
  }
}

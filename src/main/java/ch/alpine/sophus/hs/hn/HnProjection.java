// code by jph
package ch.alpine.sophus.hs.hn;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.TensorUnaryOperator;
import ch.alpine.tensor.nrm.Normalize;
import ch.alpine.tensor.sca.Chop;

/** in geomstats, the corresponding function is "regularize"
 * 
 * @see HnManifold */
/* package */ enum HnProjection implements TensorUnaryOperator {
  INSTANCE;

  private static final TensorUnaryOperator NORMALIZE = Normalize.with(HnPointNorm.INSTANCE, Chop._08);

  @Override
  public Tensor apply(Tensor x) {
    return NORMALIZE.apply(x);
  }
}

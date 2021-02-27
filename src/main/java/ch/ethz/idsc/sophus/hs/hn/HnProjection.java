// code by jph
package ch.ethz.idsc.sophus.hs.hn;

import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.api.TensorUnaryOperator;
import ch.ethz.idsc.tensor.nrm.Normalize;
import ch.ethz.idsc.tensor.sca.Chop;

/** in geomstats, the corresponding function is "regularize"
 * 
 * @see HnGeodesic */
public enum HnProjection implements TensorUnaryOperator {
  INSTANCE;

  private static final TensorUnaryOperator NORMALIZE = Normalize.with(HnPointNorm.INSTANCE, Chop._08);

  @Override
  public Tensor apply(Tensor x) {
    return NORMALIZE.apply(x);
  }
}

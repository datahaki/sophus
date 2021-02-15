// code by jph
package ch.ethz.idsc.sophus.hs.hn;

import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.Drop;
import ch.ethz.idsc.tensor.api.TensorUnaryOperator;

/** in geomstats, the corresponding function is "regularize"
 * 
 * @see HnGeodesic */
public enum HnProjection implements TensorUnaryOperator {
  INSTANCE;

  @Override
  public Tensor apply(Tensor x) {
    return HnWeierstrassCoordinate.toPoint(Drop.tail(x, 1));
  }
}

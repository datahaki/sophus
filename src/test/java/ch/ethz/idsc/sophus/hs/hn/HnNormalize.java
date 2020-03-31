// code by jph
package ch.ethz.idsc.sophus.hs.hn;

import ch.ethz.idsc.tensor.alg.Normalize;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;

/** normalization of tangent vectors */
public enum HnNormalize {
  ;
  public static final TensorUnaryOperator INSTANCE = Normalize.with(HnNorm.INSTANCE::norm);
}

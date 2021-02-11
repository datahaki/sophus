// code by jph
package ch.ethz.idsc.sophus.hs.hn;

import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.api.TensorUnaryOperator;
import ch.ethz.idsc.tensor.sca.Sqrt;

/** @see HnGeodesic */
public enum HnProjection implements TensorUnaryOperator {
  INSTANCE;

  @Override
  public Tensor apply(Tensor x) {
    // TODO not clear from where to where x is projected!?
    Scalar xn2 = HnNormSquared.INSTANCE.norm(x); // if x in H^n then equals to -1
    return x.divide(Sqrt.FUNCTION.apply(xn2.negate()));
  }
}

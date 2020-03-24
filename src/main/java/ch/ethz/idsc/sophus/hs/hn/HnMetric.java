// code by jph
package ch.ethz.idsc.sophus.hs.hn;

import ch.ethz.idsc.sophus.math.TensorMetric;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.sca.ArcCosh;

/* package */ enum HnMetric implements TensorMetric {
  INSTANCE;

  @Override
  public Scalar distance(Tensor p, Tensor q) {
    return ArcCosh.FUNCTION.apply(HnBilinearForm.between(p, q).negate());
  }
}

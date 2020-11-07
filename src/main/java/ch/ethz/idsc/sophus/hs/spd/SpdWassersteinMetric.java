// code by jph
package ch.ethz.idsc.sophus.hs.spd;

import ch.ethz.idsc.sophus.math.TensorMetric;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;

// TODO implement wasserstein metric
public enum SpdWassersteinMetric implements TensorMetric {
  INSTANCE;

  @Override
  public Scalar distance(Tensor p, Tensor q) {
    return null;
  }
}

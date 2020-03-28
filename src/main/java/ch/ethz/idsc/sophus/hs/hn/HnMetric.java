// code by jph
package ch.ethz.idsc.sophus.hs.hn;

import ch.ethz.idsc.sophus.math.TensorMetric;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.red.Max;
import ch.ethz.idsc.tensor.sca.ArcCosh;

public enum HnMetric implements TensorMetric {
  INSTANCE;

  @Override // from TensorMetric
  public Scalar distance(Tensor x, Tensor y) {
    StaticHelper.requirePoint(x);
    StaticHelper.requirePoint(y);
    Scalar v = HnBilinearForm.between(x, y).negate();
    return ArcCosh.FUNCTION.apply(Max.of(v, RealScalar.ONE));
  }
}

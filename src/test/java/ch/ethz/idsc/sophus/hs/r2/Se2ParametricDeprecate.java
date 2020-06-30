// code by jph
package ch.ethz.idsc.sophus.hs.r2;

import ch.ethz.idsc.sophus.lie.so2.So2;
import ch.ethz.idsc.sophus.math.TensorMetric;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.red.Norm;
import ch.ethz.idsc.tensor.sca.Sinc;

/** length of geodesic between p and q in SE(2) when projected to R^2
 * the projection is a circle segment */
/* package */ enum Se2ParametricDeprecate implements TensorMetric {
  INSTANCE;

  private static final Scalar HALF = RealScalar.of(0.5);

  @Override // from TensorMetric
  public Scalar distance(Tensor p, Tensor q) {
    Scalar alpha = So2.MOD.apply(p.Get(2).subtract(q.get(2))).multiply(HALF);
    return Norm._2.between(p.extract(0, 2), q.extract(0, 2)).divide(Sinc.FUNCTION.apply(alpha));
  }
}
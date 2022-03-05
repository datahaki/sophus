// code by jph
package ch.alpine.sophus.hs.r2;

import ch.alpine.sophus.lie.so2.So2;
import ch.alpine.sophus.math.TensorMetric;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.nrm.Vector2Norm;
import ch.alpine.tensor.sca.tri.Sinc;

/** length of geodesic between p and q in SE(2) when projected to R^2
 * the projection is a circle segment */
/* package */ enum Se2ParametricDeprecate implements TensorMetric {
  INSTANCE;

  private static final Scalar HALF = RealScalar.of(0.5);

  @Override // from TensorMetric
  public Scalar distance(Tensor p, Tensor q) {
    Scalar alpha = So2.MOD.apply(p.Get(2).subtract(q.get(2))).multiply(HALF);
    return Vector2Norm.between(p.extract(0, 2), q.extract(0, 2)).divide(Sinc.FUNCTION.apply(alpha));
  }
}
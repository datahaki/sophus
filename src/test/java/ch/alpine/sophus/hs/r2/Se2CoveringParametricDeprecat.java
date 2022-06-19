// code by ob
package ch.alpine.sophus.hs.r2;

import ch.alpine.sophus.math.api.TensorMetric;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Scalars;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.nrm.Vector2Norm;
import ch.alpine.tensor.sca.Abs;
import ch.alpine.tensor.sca.tri.Sin;

/** @see Se2Parametric */
/* package */ enum Se2CoveringParametricDeprecat implements TensorMetric {
  INSTANCE;

  private static final Scalar HALF = RealScalar.of(0.5);

  /** @param p element in SE2 of the form {px, py, p_heading}
   * @param q element in SE2 of the form {qx, qy, q_heading}
   * @return length of geodesic between p and q when projected to R^2 including the number of windings */
  @Override
  public Scalar distance(Tensor p, Tensor q) {
    Scalar norm = Vector2Norm.between(p.extract(0, 2), q.extract(0, 2));
    Scalar alpha = q.Get(2).subtract(p.Get(2));
    if (Scalars.isZero(alpha))
      return norm;
    Scalar ahalf = alpha.multiply(HALF);
    Scalar radius = norm.multiply(HALF).divide(Sin.FUNCTION.apply(ahalf));
    return Abs.FUNCTION.apply(radius.multiply(alpha));
  }
}

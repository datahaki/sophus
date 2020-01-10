// code by ob
package ch.ethz.idsc.sophus.hs.h2;

import ch.ethz.idsc.sophus.lie.rn.RnGeodesic;
import ch.ethz.idsc.sophus.math.SplitInterface;
import ch.ethz.idsc.tensor.NumberQ;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.sca.AbsSquared;
import ch.ethz.idsc.tensor.sca.ArcTanh;
import ch.ethz.idsc.tensor.sca.Cosh;
import ch.ethz.idsc.tensor.sca.Power;
import ch.ethz.idsc.tensor.sca.Sign;
import ch.ethz.idsc.tensor.sca.Sqrt;
import ch.ethz.idsc.tensor.sca.Tanh;

/** geodesic on 2-dimensional hyperbolic half
 * 
 * half-plane coordinates are of the form {x, y} with y strictly positive
 * 
 * Tomasz Popiel, Lyle Noakes - Bézier Curves and C^2 interpolation in Riemannian manifolds */
public enum H2Geodesic implements SplitInterface {
  INSTANCE;
  // ---
  /** p and q are vectors of length 2 with the second entry positive
   * 
   * @throws Exception if input is not valid */
  @Override // from GeodesicInterface
  public Tensor split(Tensor p, Tensor q, Scalar scalar) {
    Scalar p1 = p.Get(0);
    Scalar p2 = Sign.requirePositive(p.Get(1));
    Scalar q1 = q.Get(0);
    Scalar q2 = Sign.requirePositive(q.Get(1));
    if (p1.equals(q1)) // when p == q or p == -q
      return Tensors.of(p1, height(p2, q2, scalar));
    Scalar pq = p1.subtract(q1);
    Scalar c1 = (Scalar) p.dot(p).subtract(q.dot(q)).divide(pq.add(pq));
    Scalar p1_c1 = p1.subtract(c1);
    Scalar c2 = Sqrt.FUNCTION.apply(AbsSquared.FUNCTION.apply(p1_c1).add(p2.multiply(p2)));
    Scalar c3 = ArcTanh.FUNCTION.apply(p1_c1.divide(c2));
    if (NumberQ.of(c3)) {
      Scalar c4 = ArcTanh.FUNCTION.apply(q1.subtract(c1).divide(c2)).subtract(c3);
      return Tensors.of( //
          c1.add(c2.multiply(Tanh.FUNCTION.apply(c3.add(c4.multiply(scalar))))), //
          c2.divide(Cosh.FUNCTION.apply(c3.add(c4.multiply(scalar)))));
    }
    return Tensors.of(RnGeodesic.INSTANCE.split(p1, q1, scalar), height(p2, q2, scalar));
  }

  private static Scalar height(Scalar p2, Scalar q2, Scalar t) {
    return Power.function(t).apply(q2.divide(p2)).multiply(p2);
  }
}

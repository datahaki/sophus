// code by jph
package ch.alpine.sophus.hs.h;

import java.io.Serializable;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Append;
import ch.alpine.tensor.nrm.Vector2Norm;
import ch.alpine.tensor.sca.pow.Sqrt;

/** R^n -> Hn
 * 
 * "the hyperbolic space can be seen as the sphere of radius -1 in the
 * (n + 1)-dimensional Minkowski space"
 * 
 * "This happens to be in fact a global diffeomorphism that provides a
 * very convenient global chart of the hyperbolic space."
 * 
 * Reference:
 * "Barycentric Subspace Analysis on Manifolds"
 * by Xavier Pennec, 2016 */
public record HWeierstrassCoordinate(Tensor p) implements Serializable {
  /** @param p vector
   * @return vector of length one greater than length of x */
  public Tensor toPoint() {
    return Append.of(p, xn());
  }

  /** @param p vector of length n
   * @param v vector of length n
   * @return vector of length n + 1 */
  public Tensor toTangent(Tensor v) {
    return Append.of(v, p.dot(v).divide(xn()));
  }

  private Scalar xn() {
    return Vector2Norm.of(Append.of(p, RealScalar.ONE));
  }

  public Scalar toNorm(Tensor v) {
    Scalar pv = (Scalar) p.dot(v);
    return Sqrt.FUNCTION.apply((Scalar) v.dot(v).subtract(pv.multiply(pv).divide(RealScalar.ONE.add(p.dot(p)))));
  }
}

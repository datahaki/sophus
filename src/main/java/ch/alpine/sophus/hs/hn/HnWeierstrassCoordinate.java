// code by jph
package ch.alpine.sophus.hs.hn;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Append;
import ch.alpine.tensor.nrm.Vector2Norm;

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
public enum HnWeierstrassCoordinate {
  ;
  /** @param x vector
   * @return vector of length one greater than length of x */
  public static Tensor toPoint(Tensor x) {
    return Append.of(x, xn(x));
  }

  /** @param x vector of length n
   * @param v vector of length n
   * @return vector of length n + 1 */
  public static Tensor toTangent(Tensor x, Tensor v) {
    return Append.of(v, x.dot(v).divide(xn(x)));
  }

  private static Scalar xn(Tensor x) {
    return Vector2Norm.of(Append.of(x, RealScalar.ONE));
  }
}

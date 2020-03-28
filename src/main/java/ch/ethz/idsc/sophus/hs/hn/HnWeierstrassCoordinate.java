// code by jph
package ch.ethz.idsc.sophus.hs.hn;

import ch.ethz.idsc.sophus.lie.rn.RnNormSquared;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.Join;
import ch.ethz.idsc.tensor.sca.Sqrt;

/** R^n -> Hn
 * 
 * "the hyperbolic space can be seen as the sphere of radius -1 in the
 * (n + 1)-dimensional Minkowski space"
 * 
 * "This happen to be in fact a global diffeomorphism that provides a
 * very convenient global chart of the hyperbolic space."
 * 
 * Reference:
 * "Barycentric Subspace Analysis on Manifolds"
 * by Xavier Pennec, 2016 */
public enum HnWeierstrassCoordinate {
  ;
  public static Tensor toPoint(Tensor x) {
    Scalar x0 = Sqrt.FUNCTION.apply(RnNormSquared.INSTANCE.norm(x).add(RealScalar.ONE));
    return Join.of(Tensors.of(x0), x);
  }

  public static Tensor toTangent(Tensor x, Tensor v) {
    Scalar x0 = Sqrt.FUNCTION.apply(RnNormSquared.INSTANCE.norm(x).add(RealScalar.ONE));
    return Join.of(Tensors.of(x.dot(v).divide(x0)), v);
  }
}

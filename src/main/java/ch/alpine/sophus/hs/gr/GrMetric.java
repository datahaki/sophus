// code by jph 
package ch.alpine.sophus.hs.gr;

import ch.alpine.sophus.math.LowerVectorize0_2Norm;
import ch.alpine.sophus.math.TensorMetric;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;

/** distance(p, q) == FrobeniusNorm[new GrExponential(p).log(q)]
 * 
 * Reference:
 * "Geometric mean and geodesic regression on Grassmannians"
 * E. Batzies, K. Hueper, L. Machado, F. Silva Leite by 2015 */
public enum GrMetric implements TensorMetric {
  INSTANCE;

  @Override // from TensorMetric
  public Scalar distance(Tensor p, Tensor q) {
    return LowerVectorize0_2Norm.INSTANCE.norm(new GrExponential(p).vectorLog(q));
  }
}

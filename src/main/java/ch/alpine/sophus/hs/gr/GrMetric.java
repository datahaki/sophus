// code by jph 
package ch.alpine.sophus.hs.gr;

import ch.alpine.sophus.math.TensorMetric;
import ch.alpine.sophus.math.Vectorize0_2Norm;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.nrm.FrobeniusNorm;

/** Reference:
 * "Geometric mean and geodesic regression on Grassmannians"
 * E. Batzies, K. Hueper, L. Machado, F. Silva Leite by 2015
 * 
 * @see Vectorize0_2Norm */
public enum GrMetric implements TensorMetric {
  INSTANCE;

  @Override // from TensorMetric
  public Scalar distance(Tensor p, Tensor q) {
    return FrobeniusNorm.of(new GrExponential(p).log(q));
  }
}

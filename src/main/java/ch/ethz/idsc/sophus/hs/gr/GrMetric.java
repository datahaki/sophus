// code by jph 
package ch.ethz.idsc.sophus.hs.gr;

import ch.ethz.idsc.sophus.math.TensorMetric;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.red.Frobenius;

/** Reference:
 * "Geometric mean and geodesic regression on Grassmannians"
 * E. Batzies, K. Hueper, L. Machado, F. Silva Leite by 2015 */
public enum GrMetric implements TensorMetric {
  INSTANCE;

  @Override // from TensorMetric
  public Scalar distance(Tensor p, Tensor q) {
    return Frobenius.of(new GrExponential(p).log(q));
  }
}

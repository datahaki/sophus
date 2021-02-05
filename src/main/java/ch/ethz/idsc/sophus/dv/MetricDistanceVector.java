// code by jph
package ch.ethz.idsc.sophus.dv;

import ch.ethz.idsc.sophus.math.Genesis;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.red.Norm;

/** uses left-invariant metric on tangent space
 * 
 * the basis of the tangent space is chosen so that the metric restricted to the
 * tangent space is the bilinear form equals to the identity matrix.
 * 
 * Careful: Shepard interpolation does not reproduce linear functions.
 * (Because the inverse norms are not projected to the proper subspace.)
 * 
 * <p>Reference:
 * "Interpolation on Scattered Data in Multidimensions" in NR, 2007
 * 3.7.3 Shepard Interpolation */
public enum MetricDistanceVector implements Genesis {
  INSTANCE;

  @Override // from Genesis
  public Tensor origin(Tensor levers) {
    return Tensor.of(levers.stream().map(Norm._2::ofVector));
  }
}

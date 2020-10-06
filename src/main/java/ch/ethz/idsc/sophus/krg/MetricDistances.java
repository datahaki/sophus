// code by jph
package ch.ethz.idsc.sophus.krg;

import ch.ethz.idsc.sophus.gbc.Genesis;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.red.Norm;

/** uses left-invariant metric on tangent space
 * 
 * Careful: Shepard interpolation does not reproduce linear functions.
 * (Because the inverse norms are not projected to the proper subspace.)
 * 
 * <p>Reference:
 * "Interpolation on Scattered Data in Multidimensions" in NR, 2007
 * 3.7.3 Shepard Interpolation */
public enum MetricDistances implements Genesis {
  INSTANCE;

  @Override // from WeightingInterface
  public Tensor origin(Tensor levers) {
    return Tensor.of(levers.stream().map(Norm._2::ofVector));
  }
}

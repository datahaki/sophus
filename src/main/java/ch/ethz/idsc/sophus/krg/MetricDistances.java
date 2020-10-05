// code by jph
package ch.ethz.idsc.sophus.krg;

import java.io.Serializable;

import ch.ethz.idsc.sophus.hs.HsDesign;
import ch.ethz.idsc.sophus.hs.VectorLogManifold;
import ch.ethz.idsc.sophus.math.WeightingInterface;
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
public class MetricDistances implements WeightingInterface, Serializable {
  private static final long serialVersionUID = 912368673419897598L;
  private final HsDesign hsDesign;

  /** @param vectorLogManifold */
  public MetricDistances(VectorLogManifold vectorLogManifold) {
    hsDesign = new HsDesign(vectorLogManifold);
  }

  @Override // from WeightingInterface
  public Tensor weights(Tensor sequence, Tensor point) {
    return Tensor.of(hsDesign.stream(sequence, point) //
        .map(Norm._2::ofVector));
  }
}

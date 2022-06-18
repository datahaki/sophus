// code by jph
package ch.alpine.sophus.dv;

import java.util.Objects;

import ch.alpine.sophus.hs.HsDesign;
import ch.alpine.sophus.hs.Manifold;
import ch.alpine.sophus.hs.Sedarim;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.ScalarUnaryOperator;
import ch.alpine.tensor.nrm.NormalizeTotal;

/** Reference:
 * "Biinvariant Distance Vectors"
 * by Jan Hakenberg, 2020 */
/* package */ class GardenCoordinate implements Sedarim {
  private final HsDesign hsDesign;
  private final ScalarUnaryOperator variogram;
  private final Sedarim distances;
  private final Tensor sequence;

  /** @param manifold
   * @param variogram
   * @param sequence
   * @return */
  public GardenCoordinate(Manifold manifold, ScalarUnaryOperator variogram, Tensor sequence) {
    hsDesign = new HsDesign(manifold);
    this.variogram = Objects.requireNonNull(variogram);
    distances = new GardenDistanceVector(manifold, sequence);
    this.sequence = sequence;
  }

  @Override
  public Tensor sunder(Tensor point) {
    // building influence matrix at point is warranted since the mahalanobis forms
    // exist only at sequence points
    return StaticHelper.barycentric( //
        hsDesign.matrix(sequence, point), //
        NormalizeTotal.FUNCTION.apply(distances.sunder(point).map(variogram))); // point as input to target
  }
}

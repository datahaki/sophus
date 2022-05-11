// code by jph
package ch.alpine.sophus.gbc;

import java.util.Objects;

import ch.alpine.sophus.dv.GardenDistanceVector;
import ch.alpine.sophus.hs.HsDesign;
import ch.alpine.sophus.hs.Manifold;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.ScalarUnaryOperator;
import ch.alpine.tensor.api.TensorUnaryOperator;
import ch.alpine.tensor.nrm.NormalizeTotal;

/** Reference:
 * "Biinvariant Distance Vectors"
 * by Jan Hakenberg, 2020 */
public class GardenCoordinate implements TensorUnaryOperator {
  /** @param vectorLogManifold
   * @param variogram
   * @param sequence
   * @return */
  public static TensorUnaryOperator of( //
      Manifold vectorLogManifold, ScalarUnaryOperator variogram, Tensor sequence) {
    return new GardenCoordinate(vectorLogManifold, Objects.requireNonNull(variogram), sequence);
  }

  // ---
  private final HsDesign hsDesign;
  private final ScalarUnaryOperator variogram;
  private final TensorUnaryOperator distances;
  private final Tensor sequence;

  private GardenCoordinate(Manifold vectorLogManifold, ScalarUnaryOperator variogram, Tensor sequence) {
    hsDesign = new HsDesign(vectorLogManifold);
    this.variogram = variogram;
    distances = GardenDistanceVector.of(vectorLogManifold, sequence);
    this.sequence = sequence;
  }

  @Override
  public Tensor apply(Tensor point) {
    // building influence matrix at point is warranted since the mahalanobis forms
    // exist only at sequence points
    return StaticHelper.barycentric( //
        hsDesign.matrix(sequence, point), //
        NormalizeTotal.FUNCTION.apply(distances.apply(point).map(variogram))); // point as input to target
  }
}

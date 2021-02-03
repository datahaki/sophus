// code by jph
package ch.ethz.idsc.sophus.gbc;

import java.util.Objects;

import ch.ethz.idsc.sophus.dv.GardenDistances;
import ch.ethz.idsc.sophus.hs.HsDesign;
import ch.ethz.idsc.sophus.hs.VectorLogManifold;
import ch.ethz.idsc.sophus.math.NormalizeTotal;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.api.ScalarUnaryOperator;
import ch.ethz.idsc.tensor.api.TensorUnaryOperator;

/** Reference:
 * "Biinvariant Distance Vectors"
 * by Jan Hakenberg, 2020 */
public class GardenCoordinate implements TensorUnaryOperator, VectorField {
  private static final long serialVersionUID = 3828124845607313545L;

  /** @param vectorLogManifold
   * @param variogram
   * @param sequence
   * @return */
  public static TensorUnaryOperator of(VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram, Tensor sequence) {
    return new GardenCoordinate(vectorLogManifold, Objects.requireNonNull(variogram), sequence);
  }

  /***************************************************/
  private final HsDesign hsDesign;
  private final ScalarUnaryOperator variogram;
  private final TensorUnaryOperator distances;
  private final Tensor sequence;

  private GardenCoordinate(VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram, Tensor sequence) {
    hsDesign = new HsDesign(vectorLogManifold);
    this.variogram = variogram;
    distances = GardenDistances.of(vectorLogManifold, sequence);
    this.sequence = sequence;
  }

  @Override
  public Tensor apply(Tensor point) {
    return StaticHelper.barycentric( //
        hsDesign.matrix(sequence, point), //
        NormalizeTotal.FUNCTION.apply(distances.apply(point).map(variogram))); // point as input to target
  }
}

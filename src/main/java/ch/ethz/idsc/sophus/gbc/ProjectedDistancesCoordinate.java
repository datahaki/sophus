// code by jph
package ch.ethz.idsc.sophus.gbc;

import ch.ethz.idsc.sophus.hs.HsProjection;
import ch.ethz.idsc.sophus.hs.VectorLogManifold;
import ch.ethz.idsc.sophus.math.NormalizeTotal;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;

public class ProjectedDistancesCoordinate implements TensorUnaryOperator {
  /** @param vectorLogManifold
   * @param distances
   * @param sequence
   * @return */
  public static TensorUnaryOperator of(VectorLogManifold vectorLogManifold, TensorUnaryOperator distances, Tensor sequence) {
    return new ProjectedDistancesCoordinate(vectorLogManifold, distances, sequence);
  }

  /***************************************************/
  private final HsProjection hsProjection;
  private final TensorUnaryOperator distances;
  private final Tensor sequence;

  private ProjectedDistancesCoordinate(VectorLogManifold vectorLogManifold, TensorUnaryOperator distances, Tensor sequence) {
    hsProjection = new HsProjection(vectorLogManifold);
    this.distances = distances;
    this.sequence = sequence;
  }

  @Override
  public Tensor apply(Tensor point) {
    Tensor weights = NormalizeTotal.FUNCTION.apply(distances.apply(point));
    return NormalizeTotal.FUNCTION.apply(hsProjection.projection(sequence, point).dot(weights));
  }
}

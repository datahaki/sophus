// code by jph
package ch.ethz.idsc.sophus.gbc;

import ch.ethz.idsc.sophus.hs.HsProjection;
import ch.ethz.idsc.sophus.hs.VectorLogManifold;
import ch.ethz.idsc.sophus.krg.Mahalanobis2Distances;
import ch.ethz.idsc.sophus.math.NormalizeTotal;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;
import ch.ethz.idsc.tensor.sca.ScalarUnaryOperator;

public class Mahalanobis2Coordinate implements TensorUnaryOperator {
  /** @param vectorLogManifold
   * @param variogram
   * @param sequence
   * @return */
  public static TensorUnaryOperator of(VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram, Tensor sequence) {
    return new Mahalanobis2Coordinate(vectorLogManifold, variogram, sequence);
  }

  /***************************************************/
  private final TensorUnaryOperator distances;
  private final HsProjection hsProjection;
  private final Tensor sequence;

  private Mahalanobis2Coordinate(VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram, Tensor sequence) {
    distances = Mahalanobis2Distances.of(vectorLogManifold, variogram, sequence);
    hsProjection = new HsProjection(vectorLogManifold);
    this.sequence = sequence;
  }

  @Override
  public Tensor apply(Tensor point) {
    Tensor weights = NormalizeTotal.FUNCTION.apply(distances.apply(point));
    return NormalizeTotal.FUNCTION.apply(hsProjection.projection(sequence, point).dot(weights));
  }
}

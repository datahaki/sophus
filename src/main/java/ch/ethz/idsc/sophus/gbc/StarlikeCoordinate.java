// code by jph
package ch.ethz.idsc.sophus.gbc;

import ch.ethz.idsc.sophus.hs.HsProjection;
import ch.ethz.idsc.sophus.hs.VectorLogManifold;
import ch.ethz.idsc.sophus.krg.StarlikeDistances;
import ch.ethz.idsc.sophus.math.NormalizeTotal;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;
import ch.ethz.idsc.tensor.sca.ScalarUnaryOperator;

/** Mahalanobis form at each p_i */
public class StarlikeCoordinate implements TensorUnaryOperator {
  /** @param vectorLogManifold
   * @param variogram
   * @param sequence
   * @return */
  public static TensorUnaryOperator of(VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram, Tensor sequence) {
    return new StarlikeCoordinate(vectorLogManifold, variogram, sequence);
  }

  /***************************************************/
  private final TensorUnaryOperator distances;
  private final HsProjection hsProjection;
  private final Tensor sequence;

  private StarlikeCoordinate(VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram, Tensor sequence) {
    distances = StarlikeDistances.of(vectorLogManifold, variogram, sequence);
    hsProjection = new HsProjection(vectorLogManifold);
    this.sequence = sequence;
  }

  @Override
  public Tensor apply(Tensor point) {
    Tensor weights = NormalizeTotal.FUNCTION.apply(distances.apply(point));
    return NormalizeTotal.FUNCTION.apply(hsProjection.projection(sequence, point).dot(weights));
  }
}

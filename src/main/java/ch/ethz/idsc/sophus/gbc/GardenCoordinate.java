// code by jph
package ch.ethz.idsc.sophus.gbc;

import ch.ethz.idsc.sophus.hs.HsLevers;
import ch.ethz.idsc.sophus.hs.VectorLogManifold;
import ch.ethz.idsc.sophus.krg.GardenDistances;
import ch.ethz.idsc.sophus.math.NormalizeTotal;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;
import ch.ethz.idsc.tensor.sca.ScalarUnaryOperator;

public class GardenCoordinate implements TensorUnaryOperator {
  /** @param vectorLogManifold
   * @param variogram
   * @param sequence
   * @return */
  public static TensorUnaryOperator of(VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram, Tensor sequence) {
    return new GardenCoordinate(vectorLogManifold, variogram, sequence);
  }

  /***************************************************/
  private final HsLevers hsLevers;
  private final TensorUnaryOperator target;
  private final Tensor sequence;

  private GardenCoordinate(VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram, Tensor sequence) {
    hsLevers = new HsLevers(vectorLogManifold);
    target = GardenDistances.of(vectorLogManifold, variogram, sequence);
    this.sequence = sequence;
  }

  @Override
  public Tensor apply(Tensor point) {
    return StaticHelper.barycentric( //
        NormalizeTotal.FUNCTION.apply(target.apply(point)), // point as input to target
        hsLevers.levers(sequence, point));
  }
}
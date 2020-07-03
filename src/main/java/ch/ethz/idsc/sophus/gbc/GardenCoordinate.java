// code by jph
package ch.ethz.idsc.sophus.gbc;

import ch.ethz.idsc.sophus.hs.HsDesign;
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
  private final HsDesign hsDesign;
  private final ScalarUnaryOperator variogram;
  private final TensorUnaryOperator target;
  private final Tensor sequence;

  private GardenCoordinate(VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram, Tensor sequence) {
    hsDesign = new HsDesign(vectorLogManifold);
    this.variogram = variogram;
    target = GardenDistances.of(vectorLogManifold, sequence);
    this.sequence = sequence;
  }

  @Override
  public Tensor apply(Tensor point) {
    return StaticHelper.barycentric( //
        NormalizeTotal.FUNCTION.apply(target.apply(point).map(variogram)), // point as input to target
        hsDesign.matrix(sequence, point));
  }
}

// code by jph
package ch.ethz.idsc.sophus.gbc;

import ch.ethz.idsc.sophus.hs.VectorLogManifold;
import ch.ethz.idsc.sophus.krg.InversePowerVariogram;
import ch.ethz.idsc.sophus.krg.Relative2Distances;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;
import ch.ethz.idsc.tensor.sca.ScalarUnaryOperator;

/** biinvariant coordinate
 * 
 * @see InversePowerVariogram */
public class Relative2Coordinate implements TensorUnaryOperator {
  /** @param vectorLogManifold
   * @param variogram for instance power to minus two
   * @param sequence */
  public static TensorUnaryOperator of(VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram, Tensor sequence) {
    return new Relative2Coordinate(vectorLogManifold, variogram, sequence);
  }

  /***************************************************/
  private final Relative2Distances relative2Distances;

  private Relative2Coordinate(VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram, Tensor sequence) {
    relative2Distances = new Relative2Distances(vectorLogManifold, variogram, sequence);
  }

  @Override
  public Tensor apply(Tensor point) {
    return relative2Distances.biinvariantVector(point).coordinate();
  }
}

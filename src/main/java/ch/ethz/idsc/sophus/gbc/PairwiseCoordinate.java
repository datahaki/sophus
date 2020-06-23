// code by jph
package ch.ethz.idsc.sophus.gbc;

import ch.ethz.idsc.sophus.hs.VectorLogManifold;
import ch.ethz.idsc.sophus.krg.InversePowerVariogram;
import ch.ethz.idsc.sophus.krg.PairwiseDistances;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;
import ch.ethz.idsc.tensor.sca.ScalarUnaryOperator;

/** biinvariant coordinate
 * 
 * @see InversePowerVariogram */
public class PairwiseCoordinate implements TensorUnaryOperator {
  /** @param vectorLogManifold
   * @param variogram for instance power to minus two
   * @param sequence */
  public static TensorUnaryOperator of(VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram, Tensor sequence) {
    return new PairwiseCoordinate(vectorLogManifold, variogram, sequence);
  }

  /***************************************************/
  private final PairwiseDistances pairwiseDistances;

  private PairwiseCoordinate(VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram, Tensor sequence) {
    pairwiseDistances = PairwiseDistances.frobenius(vectorLogManifold, variogram, sequence);
  }

  @Override
  public Tensor apply(Tensor point) {
    return pairwiseDistances.biinvariantVector(point).coordinate();
  }
}

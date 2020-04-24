// code by jph
package ch.ethz.idsc.sophus.krg;

import java.io.Serializable;

import ch.ethz.idsc.sophus.hs.FlattenLogManifold;
import ch.ethz.idsc.sophus.math.WeightingInterface;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.red.Norm;
import ch.ethz.idsc.tensor.sca.ScalarUnaryOperator;

/** uses left-invariant metric on tangent space
 * 
 * @see RelativeDistances */
/* package */ class AbsoluteDistances implements WeightingInterface, Serializable {
  private final FlattenLogManifold flattenLogManifold;
  private final ScalarUnaryOperator variogram;

  /** @param flattenLogManifold
   * @param variogram
   * @param sequence */
  public AbsoluteDistances(FlattenLogManifold flattenLogManifold, ScalarUnaryOperator variogram) {
    this.flattenLogManifold = flattenLogManifold;
    this.variogram = variogram;
  }

  @Override // from WeightingInterface
  public Tensor weights(Tensor sequence, Tensor point) {
    return Tensor.of(sequence.stream() //
        .map(flattenLogManifold.logAt(point)::flattenLog) //
        .map(Norm._2::ofVector) //
        .map(variogram));
  }
}

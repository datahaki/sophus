// code by jph
package ch.ethz.idsc.sophus.krg;

import java.io.Serializable;

import ch.ethz.idsc.sophus.hs.FlattenLogManifold;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.red.Norm;
import ch.ethz.idsc.tensor.sca.ScalarUnaryOperator;

/** uses left invariant metric on tangent space
 * 
 * @see ProjectPseudoDistances */
/* package */ class LognormPseudoDistances implements PseudoDistances, Serializable {
  private final FlattenLogManifold flattenLogManifold;
  private final ScalarUnaryOperator variogram;
  private final Tensor sequence;

  /** @param flattenLogManifold
   * @param variogram
   * @param sequence */
  public LognormPseudoDistances(FlattenLogManifold flattenLogManifold, ScalarUnaryOperator variogram, Tensor sequence) {
    this.flattenLogManifold = flattenLogManifold;
    this.variogram = variogram;
    this.sequence = sequence;
  }

  @Override // from PseudoDistances
  public Tensor pseudoDistances(Tensor point) {
    return Tensor.of(sequence.stream() //
        .map(flattenLogManifold.logAt(point)::flattenLog) //
        .map(Norm._2::ofVector) //
        .map(variogram));
  }
}

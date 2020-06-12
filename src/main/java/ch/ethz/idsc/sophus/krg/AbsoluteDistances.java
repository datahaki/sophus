// code by jph
package ch.ethz.idsc.sophus.krg;

import java.io.Serializable;
import java.util.Objects;

import ch.ethz.idsc.sophus.hs.HsLevers;
import ch.ethz.idsc.sophus.hs.VectorLogManifold;
import ch.ethz.idsc.sophus.math.WeightingInterface;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.red.Norm;
import ch.ethz.idsc.tensor.sca.ScalarUnaryOperator;

/** uses left-invariant metric on tangent space
 * 
 * @see DiagonalDistances */
/* package */ class AbsoluteDistances implements WeightingInterface, Serializable {
  private final HsLevers hsLevers;
  private final ScalarUnaryOperator variogram;

  /** @param vectorLogManifold
   * @param variogram */
  public AbsoluteDistances(VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram) {
    hsLevers = new HsLevers(vectorLogManifold);
    this.variogram = Objects.requireNonNull(variogram);
  }

  @Override // from WeightingInterface
  public Tensor weights(Tensor sequence, Tensor point) {
    return Tensor.of(hsLevers.levers(sequence, point).stream() //
        .map(Norm._2::ofVector) //
        .map(variogram));
  }
}

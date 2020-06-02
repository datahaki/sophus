// code by jph
package ch.ethz.idsc.sophus.krg;

import java.io.Serializable;

import ch.ethz.idsc.sophus.hs.VectorLogManifold;
import ch.ethz.idsc.sophus.math.WeightingInterface;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.red.Norm;
import ch.ethz.idsc.tensor.sca.ScalarUnaryOperator;

/** uses left-invariant metric on tangent space
 * 
 * @see RelativeDistances */
/* package */ class AbsoluteDistances implements WeightingInterface, Serializable {
  private final VectorLogManifold vectorLogManifold;
  private final ScalarUnaryOperator variogram;

  /** @param vectorLogManifold
   * @param variogram
   * @param sequence */
  public AbsoluteDistances(VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram) {
    this.vectorLogManifold = vectorLogManifold;
    this.variogram = variogram;
  }

  @Override // from WeightingInterface
  public Tensor weights(Tensor sequence, Tensor point) {
    return Tensor.of(sequence.stream() //
        .map(vectorLogManifold.logAt(point)::vectorLog) //
        .map(Norm._2::ofVector) //
        .map(variogram));
  }
}

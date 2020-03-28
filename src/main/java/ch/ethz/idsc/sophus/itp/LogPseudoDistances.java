// code by jph
package ch.ethz.idsc.sophus.itp;

import java.io.Serializable;

import ch.ethz.idsc.sophus.hs.FlattenLogManifold;
import ch.ethz.idsc.sophus.lie.rn.RnNorm;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.sca.ScalarUnaryOperator;

public class LogPseudoDistances implements PseudoDistances, Serializable {
  private final FlattenLogManifold flattenLogManifold;
  private final ScalarUnaryOperator variogram;
  private final Tensor sequence;

  public LogPseudoDistances(FlattenLogManifold flattenLogManifold, ScalarUnaryOperator variogram, Tensor sequence) {
    this.flattenLogManifold = flattenLogManifold;
    this.variogram = variogram;
    this.sequence = sequence;
  }

  @Override // from PseudoDistances
  public Tensor pseudoDistances(Tensor point) {
    return Tensor.of(sequence.stream() //
        .map(flattenLogManifold.logAt(point)::flattenLog) //
        .map(RnNorm.INSTANCE::norm) //
        .map(variogram));
  }
}

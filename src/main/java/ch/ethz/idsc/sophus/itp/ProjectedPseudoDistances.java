// code by jph
package ch.ethz.idsc.sophus.itp;

import java.io.Serializable;

import ch.ethz.idsc.sophus.lie.rn.RnNorm;
import ch.ethz.idsc.sophus.math.win.ProjectionInterface;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.mat.IdentityMatrix;
import ch.ethz.idsc.tensor.sca.ScalarUnaryOperator;

public class ProjectedPseudoDistances implements PseudoDistances, Serializable {
  private final ProjectionInterface projectionInterface;
  private final ScalarUnaryOperator variogram;
  private final Tensor sequence;

  public ProjectedPseudoDistances(ProjectionInterface projectionInterface, ScalarUnaryOperator variogram, Tensor sequence) {
    this.projectionInterface = projectionInterface;
    this.variogram = variogram;
    this.sequence = sequence;
  }

  @Override // from PseudoDistances
  public Tensor pseudoDistances(Tensor point) {
    Tensor projection = IdentityMatrix.of(sequence.length()).subtract(projectionInterface.projection(sequence, point));
    return Tensor.of(projection.stream() //
        .map(RnNorm.INSTANCE::norm) //
        .map(variogram));
  }
}

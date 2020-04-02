// code by jph
package ch.ethz.idsc.sophus.itp;

import java.io.Serializable;

import ch.ethz.idsc.sophus.hs.ProjectionInterface;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.mat.IdentityMatrix;
import ch.ethz.idsc.tensor.red.Norm;
import ch.ethz.idsc.tensor.sca.ScalarUnaryOperator;

/** @see LognormPseudoDistances */
public class ProjectPseudoDistances implements PseudoDistances, Serializable {
  private final ProjectionInterface projectionInterface;
  private final ScalarUnaryOperator variogram;
  private final Tensor sequence;

  /** @param projectionInterface
   * @param variogram
   * @param sequence */
  public ProjectPseudoDistances( //
      ProjectionInterface projectionInterface, ScalarUnaryOperator variogram, Tensor sequence) {
    this.projectionInterface = projectionInterface;
    this.variogram = variogram;
    this.sequence = sequence;
  }

  @Override // from PseudoDistances
  public Tensor pseudoDistances(Tensor point) {
    Tensor projection = projectionInterface.projection(sequence, point);
    return Tensor.of(IdentityMatrix.of(sequence.length()).subtract(projection).stream() //
        .map(Norm._2::ofVector) //
        .map(variogram));
  }
}

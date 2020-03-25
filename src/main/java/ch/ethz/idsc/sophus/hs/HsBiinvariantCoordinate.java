// code by jph
package ch.ethz.idsc.sophus.hs;

import java.util.Objects;

import ch.ethz.idsc.sophus.lie.FlattenLogManifold;
import ch.ethz.idsc.sophus.math.NormalizeAffine;
import ch.ethz.idsc.sophus.math.TensorNorm;
import ch.ethz.idsc.sophus.math.win.InverseNorm;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.mat.IdentityMatrix;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;

/** Reference:
 * "Biinvariant Generalized Barycentric Coordinates on Lie Groups"
 * by Jan Hakenberg, 2020 */
public final class HsBiinvariantCoordinate extends HsProjection implements ProjectedCoordinate {
  private final TensorUnaryOperator target;

  /** @param tensorNorm */
  public HsBiinvariantCoordinate(FlattenLogManifold flattenLogManifold, TensorNorm tensorNorm) {
    super(flattenLogManifold);
    this.target = InverseNorm.of(tensorNorm);
  }

  /** @param target typically {@link InverseNorm} */
  public HsBiinvariantCoordinate(FlattenLogManifold flattenLogManifold, TensorUnaryOperator target) {
    super(flattenLogManifold);
    this.target = Objects.requireNonNull(target);
  }

  @Override // from BarycentricCoordinate
  public final Tensor weights(Tensor sequence, Tensor point) {
    Tensor projection = projection(sequence, point);
    return NormalizeAffine.of( //
        target.apply(IdentityMatrix.of(sequence.length()).subtract(projection)), // typically: inverse norm of rows
        projection); // projection enforces linear reproduction
  }
}

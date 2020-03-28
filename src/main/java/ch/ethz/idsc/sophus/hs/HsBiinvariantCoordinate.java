// code by jph
package ch.ethz.idsc.sophus.hs;

import java.util.Objects;

import ch.ethz.idsc.sophus.lie.rn.RnNorm;
import ch.ethz.idsc.sophus.lie.rn.RnNormSquared;
import ch.ethz.idsc.sophus.math.NormalizeAffine;
import ch.ethz.idsc.sophus.math.id.InverseDiagonal;
import ch.ethz.idsc.sophus.math.id.InverseNorm;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.ConstantArray;
import ch.ethz.idsc.tensor.mat.IdentityMatrix;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;

/** Reference:
 * "Biinvariant Generalized Barycentric Coordinates on Lie Groups"
 * by Jan Hakenberg, 2020 */
public final class HsBiinvariantCoordinate extends HsProjection implements ProjectedCoordinate {
  private static final TensorUnaryOperator AFFINE = levers -> ConstantArray.of(RealScalar.ONE, levers.length());

  /** @param flattenLogManifold
   * @return */
  public static ProjectedCoordinate linear(FlattenLogManifold flattenLogManifold) {
    return new HsBiinvariantCoordinate(flattenLogManifold, InverseNorm.of(RnNorm.INSTANCE));
  }

  /** most common choice
   * 
   * @param flattenLogManifold
   * @return */
  public static ProjectedCoordinate smooth(FlattenLogManifold flattenLogManifold) {
    return new HsBiinvariantCoordinate(flattenLogManifold, InverseNorm.of(RnNormSquared.INSTANCE));
  }

  /** @param flattenLogManifold
   * @param target
   * @return */
  public static ProjectedCoordinate custom(FlattenLogManifold flattenLogManifold, TensorUnaryOperator target) {
    return new HsBiinvariantCoordinate(flattenLogManifold, target);
  }

  /** @param flattenLogManifold
   * @return */
  public static ProjectedCoordinate diagonal_linear(FlattenLogManifold flattenLogManifold) {
    return custom(flattenLogManifold, InverseDiagonal.of(RnNorm.INSTANCE));
  }

  /** @param flattenLogManifold
   * @return */
  public static ProjectedCoordinate diagonal_smooth(FlattenLogManifold flattenLogManifold) {
    return custom(flattenLogManifold, InverseDiagonal.of(RnNormSquared.INSTANCE));
  }

  /** @param flattenLogManifold
   * @return biinvariant coordinates */
  public static ProjectedCoordinate affine(FlattenLogManifold flattenLogManifold) {
    // HsBarycentricCoordinate uses more efficient matrix multiplication
    return HsBarycentricCoordinate.custom(flattenLogManifold, AFFINE);
  }

  /***************************************************/
  private final TensorUnaryOperator target;

  /** @param target typically {@link InverseNorm} */
  private HsBiinvariantCoordinate(FlattenLogManifold flattenLogManifold, TensorUnaryOperator target) {
    super(flattenLogManifold);
    this.target = Objects.requireNonNull(target);
  }

  @Override // from BarycentricCoordinate
  public Tensor weights(Tensor sequence, Tensor point) {
    Tensor projection = projection(sequence, point);
    return NormalizeAffine.of( //
        target.apply(IdentityMatrix.of(sequence.length()).subtract(projection)), // typically: inverse norm of rows
        projection); // projection enforces linear reproduction
  }
}

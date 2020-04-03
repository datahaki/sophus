// code by jph
package ch.ethz.idsc.sophus.gbc;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import ch.ethz.idsc.sophus.hs.FlattenLogManifold;
import ch.ethz.idsc.sophus.lie.rn.RnNorm;
import ch.ethz.idsc.sophus.lie.rn.RnNormSquared;
import ch.ethz.idsc.sophus.math.id.InverseDiagonal;
import ch.ethz.idsc.sophus.math.id.InverseNorm;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.ConstantArray;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;

/** relative coordinates are biinvariant
 * 
 * Reference:
 * "Biinvariant Generalized Barycentric Coordinates on Lie Groups"
 * by Jan Hakenberg, 2020 */
public final class RelativeCoordinate extends HsProjection implements ProjectedCoordinate {
  private static final Scalar ONE = RealScalar.of(1.0);
  private static final TensorUnaryOperator AFFINE = levers -> ConstantArray.of(RealScalar.ONE, levers.length());

  /** @param flattenLogManifold
   * @return */
  public static ProjectedCoordinate linear(FlattenLogManifold flattenLogManifold) {
    return new RelativeCoordinate(flattenLogManifold, InverseNorm.of(RnNorm.INSTANCE));
  }

  /** Hint: most common choice since coordinates vary smoothly
   * 
   * @param flattenLogManifold
   * @return */
  public static ProjectedCoordinate smooth(FlattenLogManifold flattenLogManifold) {
    return new RelativeCoordinate(flattenLogManifold, InverseNorm.of(RnNormSquared.INSTANCE));
  }

  /** @param flattenLogManifold
   * @param target non-null
   * @return */
  public static ProjectedCoordinate custom(FlattenLogManifold flattenLogManifold, TensorUnaryOperator target) {
    return new RelativeCoordinate(flattenLogManifold, Objects.requireNonNull(target));
  }

  /***************************************************/
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
    return AbsoluteCoordinate.custom(flattenLogManifold, AFFINE);
  }

  /***************************************************/
  private final TensorUnaryOperator target;

  private RelativeCoordinate(FlattenLogManifold flattenLogManifold, TensorUnaryOperator target) {
    super(flattenLogManifold);
    this.target = target;
  }

  @Override // from BarycentricCoordinate
  public Tensor weights(Tensor sequence, Tensor point) {
    Tensor projection = projection(sequence, point);
    AtomicInteger atomicInteger = new AtomicInteger();
    Tensor complement = Tensor.of(projection.stream() //
        .map(Tensor::negate) //
        .map(row -> {
          row.set(ONE::add, atomicInteger.getAndIncrement());
          return row;
        }));
    return NormalizeAffine.fromProjection(target.apply(complement), projection);
  }
}

// code by jph
package ch.ethz.idsc.sophus.gbc;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import ch.ethz.idsc.sophus.hs.VectorLogManifold;
import ch.ethz.idsc.sophus.math.NormalizeTotal;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.ConstantArray;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;
import ch.ethz.idsc.tensor.red.Norm;
import ch.ethz.idsc.tensor.sca.ScalarUnaryOperator;

/** relative coordinates are biinvariant
 * 
 * Reference:
 * "Biinvariant Generalized Barycentric Coordinates on Lie Groups"
 * by Jan Hakenberg, 2020 */
public final class RelativeCoordinate extends HsProjection implements ProjectedCoordinate {
  private static final Scalar ONE = RealScalar.of(1.0);
  private static final TensorUnaryOperator AFFINE = levers -> ConstantArray.of(RealScalar.ONE, levers.length());

  /** @param vectorLogManifold
   * @param variogram
   * @return */
  public static ProjectedCoordinate of(VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram) {
    Objects.requireNonNull(variogram);
    TensorUnaryOperator target = levers -> NormalizeTotal.FUNCTION.apply( //
        Tensor.of(levers.stream().map(Norm._2::ofVector).map(variogram)));
    return new RelativeCoordinate(vectorLogManifold, target);
  }

  /** @param vectorLogManifold
   * @param target non-null
   * @return */
  public static ProjectedCoordinate custom(VectorLogManifold vectorLogManifold, TensorUnaryOperator target) {
    return new RelativeCoordinate(vectorLogManifold, Objects.requireNonNull(target));
  }

  /***************************************************/
  /** @param vectorLogManifold
   * @return */
  public static ProjectedCoordinate diagonal(VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram) {
    return custom(vectorLogManifold, InverseDiagonal.of(variogram));
  }

  /** @param vectorLogManifold
   * @return biinvariant coordinates */
  public static ProjectedCoordinate affine(VectorLogManifold vectorLogManifold) {
    // HsBarycentricCoordinate uses more efficient matrix multiplication
    return AbsoluteCoordinate.custom(vectorLogManifold, AFFINE);
  }

  /***************************************************/
  private final TensorUnaryOperator target;

  private RelativeCoordinate(VectorLogManifold vectorLogManifold, TensorUnaryOperator target) {
    super(vectorLogManifold);
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

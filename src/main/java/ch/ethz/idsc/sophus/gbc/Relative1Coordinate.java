// code by jph
package ch.ethz.idsc.sophus.gbc;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

import ch.ethz.idsc.sophus.hs.HsProjection;
import ch.ethz.idsc.sophus.hs.VectorLogManifold;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;
import ch.ethz.idsc.tensor.sca.ScalarUnaryOperator;

/** relative coordinates are biinvariant
 * 
 * Reference:
 * "Biinvariant Generalized Barycentric Coordinates on Lie Groups"
 * by Jan Hakenberg, 2020 */
public final class Relative1Coordinate implements BarycentricCoordinate, Serializable {
  private static final Scalar ONE = RealScalar.of(1.0);

  /** @param vectorLogManifold
   * @param variogram
   * @return */
  public static BarycentricCoordinate of(VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram) {
    return new Relative1Coordinate(vectorLogManifold, new NormalizeLevers(variogram));
  }

  /***************************************************/
  private final HsProjection hsProjection;
  private final TensorUnaryOperator target;

  private Relative1Coordinate(VectorLogManifold vectorLogManifold, TensorUnaryOperator target) {
    hsProjection = new HsProjection(vectorLogManifold);
    this.target = target;
  }

  @Override // from BarycentricCoordinate
  public Tensor weights(Tensor sequence, Tensor point) {
    Tensor projection = hsProjection.projection(sequence, point);
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

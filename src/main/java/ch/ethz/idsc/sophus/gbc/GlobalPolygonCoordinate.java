// code by jph
package ch.ethz.idsc.sophus.gbc;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.BiFunction;

import ch.ethz.idsc.sophus.hs.HsDesign;
import ch.ethz.idsc.sophus.hs.VectorLogManifold;
import ch.ethz.idsc.sophus.lie.r2.Barycenter;
import ch.ethz.idsc.sophus.lie.r2.ThreePointCoordinate;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;

public class GlobalPolygonCoordinate implements BarycentricCoordinate, Serializable {
  /** @param vectorLogManifold with 2-dimensional tangent space
   * @param biFunction {@link Barycenter}
   * @return */
  public static BarycentricCoordinate of( //
      VectorLogManifold vectorLogManifold, BiFunction<Tensor, Scalar, Tensor> biFunction) {
    return new GlobalPolygonCoordinate(Objects.requireNonNull(vectorLogManifold), biFunction);
  }

  /***************************************************/
  private final HsDesign hsDesign;
  private final TensorUnaryOperator tensorUnaryOperator;

  private GlobalPolygonCoordinate( //
      VectorLogManifold vectorLogManifold, BiFunction<Tensor, Scalar, Tensor> biFunction) {
    hsDesign = new HsDesign(vectorLogManifold);
    tensorUnaryOperator = ThreePointCoordinate.of(biFunction);
  }

  @Override // from BarycentricCoordinate
  public Tensor weights(Tensor sequence, Tensor point) {
    return tensorUnaryOperator.apply(hsDesign.matrix(sequence, point));
  }
}

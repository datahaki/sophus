// code by jph
package ch.ethz.idsc.sophus.gbc;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.BiFunction;

import ch.ethz.idsc.sophus.hs.HsDesign;
import ch.ethz.idsc.sophus.hs.VectorLogManifold;
import ch.ethz.idsc.sophus.lie.r2.Polygons;
import ch.ethz.idsc.sophus.lie.r2.ThreePointCoordinate;
import ch.ethz.idsc.tensor.DoubleScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.ConstantArray;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;

public class InsidePolygonCoordinate implements BarycentricCoordinate, Serializable {
  /** @param vectorLogManifold with 2-dimensional tangent space
   * @param biFunction {@link Barycenter}
   * @return */
  public static BarycentricCoordinate of( //
      VectorLogManifold vectorLogManifold, BiFunction<Tensor, Scalar, Tensor> biFunction) {
    return new InsidePolygonCoordinate(Objects.requireNonNull(vectorLogManifold), biFunction);
  }

  /***************************************************/
  private final HsDesign hsDesign;
  private final TensorUnaryOperator tensorUnaryOperator;

  private InsidePolygonCoordinate( //
      VectorLogManifold vectorLogManifold, BiFunction<Tensor, Scalar, Tensor> biFunction) {
    hsDesign = new HsDesign(vectorLogManifold);
    tensorUnaryOperator = ThreePointCoordinate.of(biFunction);
  }

  @Override // from BarycentricCoordinate
  public Tensor weights(Tensor sequence, Tensor point) {
    Tensor design = hsDesign.matrix(sequence, point);
    return Polygons.isInside(design) //
        ? tensorUnaryOperator.apply(design)
        : ConstantArray.of(DoubleScalar.INDETERMINATE, sequence.length());
  }
}

// code by jph
package ch.ethz.idsc.sophus.gbc;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.BiFunction;

import ch.ethz.idsc.sophus.hs.HsDesign;
import ch.ethz.idsc.sophus.hs.VectorLogManifold;
import ch.ethz.idsc.sophus.lie.r2.Barycenter;
import ch.ethz.idsc.sophus.lie.r2.Polygons;
import ch.ethz.idsc.sophus.lie.r2.ThreePointCoordinate;
import ch.ethz.idsc.tensor.DoubleScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.ConstantArray;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;

public class InsidePolygonCoordinate implements BarycentricCoordinate, Serializable {
  private static final long serialVersionUID = -723004141774093449L;

  /** @param vectorLogManifold with 2-dimensional tangent space
   * @param biFunction {@link Barycenter}
   * @return */
  public static BarycentricCoordinate of( //
      VectorLogManifold vectorLogManifold, BiFunction<Tensor, Scalar, Tensor> biFunction) {
    return new InsidePolygonCoordinate( //
        vectorLogManifold, //
        ThreePointCoordinate.of(biFunction));
  }

  /***************************************************/
  private final HsDesign hsDesign;
  private final TensorUnaryOperator tensorUnaryOperator;

  /** @param vectorLogManifold
   * @param tensorUnaryOperator that evaluates polygon coordinates at zero (0, 0) */
  public InsidePolygonCoordinate( //
      VectorLogManifold vectorLogManifold, TensorUnaryOperator tensorUnaryOperator) {
    hsDesign = new HsDesign(vectorLogManifold);
    this.tensorUnaryOperator = Objects.requireNonNull(tensorUnaryOperator);
  }

  @Override // from BarycentricCoordinate
  public Tensor weights(Tensor sequence, Tensor point) {
    Tensor levers = hsDesign.matrix(sequence, point);
    return Polygons.isInside(levers) //
        ? tensorUnaryOperator.apply(levers)
        : ConstantArray.of(DoubleScalar.INDETERMINATE, levers.length());
  }
}

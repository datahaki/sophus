// code by jph
package ch.ethz.idsc.sophus.lie.r2;

import java.util.function.BiFunction;

import ch.ethz.idsc.sophus.gbc.BarycentricCoordinate;
import ch.ethz.idsc.sophus.gbc.HsCoordinates;
import ch.ethz.idsc.sophus.hs.VectorLogManifold;
import ch.ethz.idsc.sophus.math.NormalizeTotal;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;

/** Three-point coordinates are also referred to as "Complete family of coordinates"
 * 
 * References:
 * "Generalized Barycentric Coordinates in Computer Graphics and Computational Mechanics"
 * by Kai Hormann, N. Sukumar, 2017
 * 
 * "Power Coordinates: A Geometric Construction of Barycentric Coordinates on Convex Polytopes"
 * by Max Budninskiy, Beibei Liu, Yiying Tong, Mathieu Desbrun, 2016 */
public enum ThreePointCoordinate {
  ;
  /** @param biFunction
   * @return */
  public static TensorUnaryOperator of(BiFunction<Tensor, Scalar, Tensor> biFunction) {
    TensorUnaryOperator tensorUnaryOperator = ThreePointHomogeneous.of(biFunction);
    return levers -> NormalizeTotal.FUNCTION.apply(tensorUnaryOperator.apply(levers));
  }

  /** @param vectorLogManifold with 2-dimensional tangent space
   * @param biFunction {@link Barycenter}
   * @return */
  public static BarycentricCoordinate of( //
      VectorLogManifold vectorLogManifold, BiFunction<Tensor, Scalar, Tensor> biFunction) {
    return HsCoordinates.wrap(vectorLogManifold, of(biFunction));
  }
}

// code by jph
package ch.ethz.idsc.sophus.lie.r2;

import java.io.Serializable;
import java.util.function.BiFunction;

import ch.ethz.idsc.sophus.gbc.Genesis;
import ch.ethz.idsc.sophus.gbc.HsCoordinates;
import ch.ethz.idsc.sophus.math.NormalizeTotal;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;

/** Three-point coordinates are also referred to as "Complete family of coordinates"
 * 
 * References:
 * "Generalized Barycentric Coordinates in Computer Graphics and Computational Mechanics"
 * by Kai Hormann, N. Sukumar, 2017
 * 
 * "Power Coordinates: A Geometric Construction of Barycentric Coordinates on Convex Polytopes"
 * by Max Budninskiy, Beibei Liu, Yiying Tong, Mathieu Desbrun, 2016
 * 
 * @see HsCoordinates
 * @see InsidePolygonCoordinate */
public enum ThreePointCoordinate {
  ;
  /** @param biFunction
   * @return */
  public static Genesis of(BiFunction<Tensor, Scalar, Tensor> biFunction) {
    Genesis genesis = ThreePointWeighting.of(biFunction);
    return (Genesis & Serializable) //
    levers -> NormalizeTotal.FUNCTION.apply(genesis.origin(levers));
  }
}

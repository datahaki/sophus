// code by jph
package ch.alpine.sophus.gbc.d2;

import java.io.Serializable;
import java.util.function.BiFunction;

import ch.alpine.sophus.gbc.HsCoordinates;
import ch.alpine.sophus.math.Genesis;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.nrm.NormalizeTotal;

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
    Genesis genesis = new ThreePointWeighting(biFunction);
    return (Genesis & Serializable) //
    levers -> NormalizeTotal.FUNCTION.apply(genesis.origin(levers));
  }
}

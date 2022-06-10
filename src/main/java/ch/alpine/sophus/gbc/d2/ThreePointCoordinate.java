// code by jph
package ch.alpine.sophus.gbc.d2;

import java.io.Serializable;

import ch.alpine.sophus.gbc.HsCoordinates;
import ch.alpine.sophus.hs.Genesis;
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
  public static Genesis of(ThreePointScaling biFunction) {
    Genesis genesis = new ThreePointWeighting(biFunction);
    return (Genesis & Serializable) //
    levers -> NormalizeTotal.FUNCTION.apply(genesis.origin(levers));
  }
}

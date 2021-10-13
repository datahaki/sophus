// code by jph
package ch.alpine.sophus.gbc.d2;

import ch.alpine.sophus.gbc.AffineCoordinate;
import ch.alpine.sophus.math.Genesis;

/** iterative affine coordinates generally produce non-negative weights after fewer
 * iterations than when using iterative mean value coordinates.
 * 
 * References:
 * "Affine generalised barycentric coordinates"
 * by S. Waldron, Jaen Journal on Approximation, 3(2):209-226, 2011
 * 
 * "Generalized Barycentric Coordinates in Computer Graphics and Computational Mechanics"
 * by Kai Hormann, N. Sukumar, 2017
 * 
 * @see InsidePolygonCoordinate */
public enum IterativeCircleCoordinate {
  ;
  /** @param k non-negative
   * @return iterative coordinates based on affine coordinates */
  public static Genesis of(int k) {
    return IterativeCoordinate.of(AffineCoordinate.INSTANCE, k);
  }
}

// code by jph
package ch.ethz.idsc.sophus.lie.r2;

import ch.ethz.idsc.sophus.gbc.Genesis;

/** References:
 * "Iterative coordinates"
 * by Chongyang Deng, Qingjun Chang, Kai Hormann, 2020
 * 
 * @see InsidePolygonCoordinate */
public enum IterativeMeanValueCoordinate {
  ;
  /** for k == 0 the coordinates are identical to three-point coordinates with mean value as barycenter
   * 
   * mean value coordinates are C^\infty and work for non-convex polygons
   * 
   * @param k non-negative
   * @return iterative coordinates based on mean value coordinates */
  public static Genesis of(int k) {
    return k == 0 //
        ? ThreePointCoordinate.of(Barycenter.MEAN_VALUE)
        : new IterativeCoordinate(ThreePointWeighting.of(Barycenter.MEAN_VALUE), k);
  }
}

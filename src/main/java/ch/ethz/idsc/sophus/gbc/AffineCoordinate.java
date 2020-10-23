// code by jph
package ch.ethz.idsc.sophus.gbc;

import ch.ethz.idsc.sophus.hs.VectorLogManifold;

/** biinvariant generalized barycentric coordinates that do not satisfy the lagrange property
 * 
 * Reference:
 * "Affine generalised barycentric coordinates"
 * by S. Waldron, Jaen Journal on Approximation, 3(2):209-226, 2011 */
public enum AffineCoordinate {
  ;
  /** @param vectorLogManifold
   * @return biinvariant generalized barycentric coordinates */
  public static BarycentricCoordinate of(VectorLogManifold vectorLogManifold) {
    return HsCoordinates.wrap(vectorLogManifold, MetricCoordinate.affine());
  }
}

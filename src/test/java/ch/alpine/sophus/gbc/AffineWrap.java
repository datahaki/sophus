// code by jph
package ch.alpine.sophus.gbc;

import ch.alpine.sophus.hs.VectorLogManifold;

/** biinvariant generalized barycentric coordinates that do not satisfy the Lagrange property
 * 
 * Reference:
 * "Affine generalised barycentric coordinates"
 * by S. Waldron, Jaen Journal on Approximation, 3(2):209-226, 2011 */
public enum AffineWrap {
  ;
  /** @param vectorLogManifold
   * @return biinvariant generalized barycentric coordinates */
  public static BarycentricCoordinate of(VectorLogManifold vectorLogManifold) {
    return HsCoordinates.wrap(vectorLogManifold, AffineCoordinate.INSTANCE);
  }
}

// code by jph
package ch.alpine.sophus.ref.d2;

import ch.alpine.sophus.srf.SurfaceMesh;

@FunctionalInterface
public interface SurfaceMeshRefinement {
  /** @param surfaceMesh
   * @return */
  SurfaceMesh refine(SurfaceMesh surfaceMesh);
}

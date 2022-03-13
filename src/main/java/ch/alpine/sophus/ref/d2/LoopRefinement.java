// code by jph
package ch.alpine.sophus.ref.d2;

import java.io.Serializable;

import ch.alpine.sophus.bm.BiinvariantMean;
import ch.alpine.sophus.srf.MeshStructure;
import ch.alpine.sophus.srf.SurfaceMesh;
import ch.alpine.tensor.ext.Integers;

public record LoopRefinement(BiinvariantMean biinvariantMean) //
    implements SurfaceMeshRefinement, Serializable {
  @Override
  public SurfaceMesh refine(SurfaceMesh surfaceMesh) {
    {
      surfaceMesh.faces().stream().forEach(a -> Integers.requireEquals(a.length, 3));
    }
    MeshStructure meshStructure = new MeshStructure(surfaceMesh);
    for (int index = 0; index < surfaceMesh.vrt.length(); ++index) {
      // meshStructure.ring(null)
    }
    SurfaceMesh out = new SurfaceMesh();
    return out;
  }
}

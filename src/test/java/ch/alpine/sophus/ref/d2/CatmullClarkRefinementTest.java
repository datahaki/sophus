// code by jph
package ch.alpine.sophus.ref.d2;

import java.io.IOException;

import ch.alpine.sophus.lie.se2c.Se2CoveringBiinvariantMean;
import ch.alpine.sophus.srf.SurfaceMesh;
import ch.alpine.sophus.usr.AssertFail;
import ch.alpine.tensor.ext.Serialization;
import junit.framework.TestCase;

public class CatmullClarkRefinementTest extends TestCase {
  public void testSimple() throws ClassNotFoundException, IOException {
    SurfaceMeshRefinement surfaceMeshRefinement = //
        Serialization.copy(CatmullClarkRefinement.of(Se2CoveringBiinvariantMean.INSTANCE));
    SurfaceMesh surfaceMesh = surfaceMeshRefinement.refine(SurfaceMeshExamples.quads6());
    assertEquals(surfaceMesh.faces().size(), 24);
    assertEquals(surfaceMesh.vrt.length(), 35);
  }

  public void testFailNull() {
    AssertFail.of(() -> CatmullClarkRefinement.of(null));
  }
}

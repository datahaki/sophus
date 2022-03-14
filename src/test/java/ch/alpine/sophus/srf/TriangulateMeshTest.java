// code by jph
package ch.alpine.sophus.srf;

import ch.alpine.sophus.srf.io.PlyFormat;
import ch.alpine.tensor.io.ResourceData;
import junit.framework.TestCase;

public class TriangulateMeshTest extends TestCase {
  public void testSimple() {
    SurfaceMesh surfaceMesh = PlyFormat.parse(ResourceData.lines("/io/mesh/unitcube.ply"));
    surfaceMesh = TriangulateMesh.of(surfaceMesh);
    assertTrue(surfaceMesh.boundary().isEmpty());
    assertEquals(surfaceMesh.faces().size(), 12);
  }
}

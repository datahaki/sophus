// code by jph
package ch.alpine.sophus.srf.io;

import ch.alpine.sophus.srf.SurfaceMesh;
import ch.alpine.tensor.io.ResourceData;
import junit.framework.TestCase;

public class PlyFormatTest extends TestCase {
  public void testSimple() {
    SurfaceMesh surfaceMesh = PlyFormat.parse(ResourceData.lines("/io/mesh/unitcube.ply"));
    assertEquals(surfaceMesh.vrt.length(), 8);
    assertEquals(surfaceMesh.faces().size(), 6);
  }
}

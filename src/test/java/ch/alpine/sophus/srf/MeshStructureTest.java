// code by jph
package ch.alpine.sophus.srf;

import java.util.List;

import ch.alpine.sophus.math.DirectedEdge;
import ch.alpine.sophus.srf.io.PlyFormat;
import ch.alpine.tensor.io.ResourceData;
import junit.framework.TestCase;

public class MeshStructureTest extends TestCase {
  public void testSimple() {
    SurfaceMesh surfaceMesh = PlyFormat.parse(ResourceData.lines("/io/mesh/unitcube.ply"));
    MeshStructure meshStructure = new MeshStructure(surfaceMesh);
    {
      List<DirectedEdge> list = meshStructure.ring(new DirectedEdge(0, 3));
      assertEquals(list.size(), 3);
      assertEquals(list.get(0), new DirectedEdge(0, 3));
      assertEquals(list.get(1), new DirectedEdge(0, 4));
      assertEquals(list.get(2), new DirectedEdge(0, 1));
    }
    {
      List<DirectedEdge> list = meshStructure.ring(new DirectedEdge(2, 6));
      assertEquals(list.size(), 3);
      assertEquals(list.get(0), new DirectedEdge(2, 6));
      assertEquals(list.get(1), new DirectedEdge(2, 3));
      assertEquals(list.get(2), new DirectedEdge(2, 1));
    }
  }
}

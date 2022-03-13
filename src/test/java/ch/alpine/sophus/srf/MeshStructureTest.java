// code by jph
package ch.alpine.sophus.srf;

import java.util.List;

import ch.alpine.sophus.math.IntDirectedEdge;
import ch.alpine.sophus.srf.io.PlyFormat;
import ch.alpine.tensor.io.ResourceData;
import junit.framework.TestCase;

public class MeshStructureTest extends TestCase {
  public void testSimple() {
    SurfaceMesh surfaceMesh = PlyFormat.parse(ResourceData.lines("/io/mesh/unitcube.ply"));
    MeshStructure meshStructure = new MeshStructure(surfaceMesh);
    {
      List<IntDirectedEdge> list = meshStructure.ring(new IntDirectedEdge(0, 3));
      assertEquals(list.size(), 3);
      assertEquals(list.get(0), new IntDirectedEdge(0, 3));
      assertEquals(list.get(1), new IntDirectedEdge(0, 4));
      assertEquals(list.get(2), new IntDirectedEdge(0, 1));
    }
    {
      List<IntDirectedEdge> list = meshStructure.ring(new IntDirectedEdge(2, 6));
      assertEquals(list.size(), 3);
      assertEquals(list.get(0), new IntDirectedEdge(2, 6));
      assertEquals(list.get(1), new IntDirectedEdge(2, 3));
      assertEquals(list.get(2), new IntDirectedEdge(2, 1));
    }
  }
}

// code by jph
package ch.alpine.sophus.srf;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.srf.io.PlyFormat;
import ch.alpine.tensor.io.ResourceData;

public class TriangulateMeshTest {
  @Test
  public void testSimple() {
    SurfaceMesh surfaceMesh = PlyFormat.parse(ResourceData.lines("/io/mesh/unitcube.ply"));
    surfaceMesh = TriangulateMesh.of(surfaceMesh);
    assertTrue(surfaceMesh.boundary().isEmpty());
    assertEquals(surfaceMesh.faces().size(), 12);
  }
}

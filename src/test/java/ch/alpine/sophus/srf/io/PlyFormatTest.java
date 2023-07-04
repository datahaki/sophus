// code by jph
package ch.alpine.sophus.srf.io;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.srf.SurfaceMesh;
import ch.alpine.tensor.ext.ResourceData;

class PlyFormatTest {
  @Test
  void testSimple() {
    SurfaceMesh surfaceMesh = PlyFormat.parse(ResourceData.lines("/ch/alpine/sophus/mesh/unitcube.ply"));
    assertEquals(surfaceMesh.vrt.length(), 8);
    assertEquals(surfaceMesh.faces().size(), 6);
  }
}

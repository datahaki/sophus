// code by jph
package ch.alpine.sophus.srf;

import java.io.IOException;

import ch.alpine.sophus.usr.AssertFail;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.ext.Serialization;
import junit.framework.TestCase;

public class SurfaceMeshTest extends TestCase {
  public void testEmpty() throws ClassNotFoundException, IOException {
    SurfaceMesh surfaceMesh = Serialization.copy(new SurfaceMesh());
    assertTrue(Tensors.isEmpty(surfaceMesh.polygons()));
    assertTrue(surfaceMesh.vertToFace().isEmpty());
  }

  public void testNullFail() {
    SurfaceMesh surfaceMesh = new SurfaceMesh();
    AssertFail.of(() -> surfaceMesh.addVert(null));
  }
}

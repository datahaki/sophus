// code by jph
package ch.ethz.idsc.sophus.srf;

import java.io.IOException;

import ch.ethz.idsc.sophus.usr.AssertFail;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.io.Serialization;
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

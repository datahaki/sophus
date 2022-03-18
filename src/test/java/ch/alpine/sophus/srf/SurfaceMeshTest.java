// code by jph
package ch.alpine.sophus.srf;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.usr.AssertFail;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.ext.Serialization;

public class SurfaceMeshTest {
  @Test
  public void testEmpty() throws ClassNotFoundException, IOException {
    SurfaceMesh surfaceMesh = Serialization.copy(new SurfaceMesh());
    assertTrue(Tensors.isEmpty(surfaceMesh.polygons()));
    assertTrue(surfaceMesh.vertToFace().isEmpty());
  }

  @Test
  public void testNullFail() {
    SurfaceMesh surfaceMesh = new SurfaceMesh();
    AssertFail.of(() -> surfaceMesh.addVert(null));
  }
}

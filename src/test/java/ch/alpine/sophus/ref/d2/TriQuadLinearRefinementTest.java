// code by jph
package ch.alpine.sophus.ref.d2;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.lie.rn.RnBiinvariantMean;
import ch.alpine.sophus.lie.se2c.Se2CoveringBiinvariantMean;
import ch.alpine.sophus.srf.SurfaceMesh;
import ch.alpine.tensor.alg.Array;
import ch.alpine.tensor.chq.ExactTensorQ;
import ch.alpine.tensor.ext.Serialization;
import ch.alpine.tensor.red.FirstPosition;

class TriQuadLinearRefinementTest {
  @Test
  public void testSe2CSimple() throws ClassNotFoundException, IOException {
    SurfaceMeshRefinement surfaceMeshRefinement = //
        Serialization.copy(new TriQuadLinearRefinement(Se2CoveringBiinvariantMean.INSTANCE));
    SurfaceMesh surfaceMesh = surfaceMeshRefinement.refine(SurfaceMeshExamples.quads6());
    assertEquals(surfaceMesh.faces().size(), 24);
    assertEquals(surfaceMesh.vrt.length(), 35);
  }

  @Test
  public void testRnSimple() throws ClassNotFoundException, IOException {
    SurfaceMeshRefinement surfaceMeshRefinement = //
        Serialization.copy(new TriQuadLinearRefinement(RnBiinvariantMean.INSTANCE));
    SurfaceMesh surfaceMesh = surfaceMeshRefinement.refine(SurfaceMeshExamples.quads5());
    assertEquals(surfaceMesh.faces().size(), 20);
    assertEquals(surfaceMesh.vrt.length(), 31);
    ExactTensorQ.require(surfaceMesh.vrt);
  }

  @Test
  public void testR3Simple() throws ClassNotFoundException, IOException {
    SurfaceMeshRefinement surfaceMeshRefinement = //
        Serialization.copy(new TriQuadLinearRefinement(RnBiinvariantMean.INSTANCE));
    SurfaceMesh surfaceMesh = surfaceMeshRefinement.refine(SurfaceMeshExamples.unitQuad());
    assertEquals(surfaceMesh.faces().size(), 4);
    assertEquals(surfaceMesh.vrt.length(), 9);
    assertTrue(FirstPosition.of(surfaceMesh.vrt, Array.zeros(3)).isPresent());
    ExactTensorQ.require(surfaceMesh.vrt);
  }
}

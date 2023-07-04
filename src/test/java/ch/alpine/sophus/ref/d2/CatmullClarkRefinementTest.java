// code by jph
package ch.alpine.sophus.ref.d2;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.lie.rn.RnBiinvariantMean;
import ch.alpine.sophus.lie.se2c.Se2CoveringBiinvariantMean;
import ch.alpine.sophus.srf.SurfaceMesh;
import ch.alpine.sophus.srf.io.PlyFormat;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Flatten;
import ch.alpine.tensor.chq.ExactTensorQ;
import ch.alpine.tensor.ext.ResourceData;
import ch.alpine.tensor.ext.Serialization;
import ch.alpine.tensor.red.Mean;

class CatmullClarkRefinementTest {
  @Test
  void testSimple() throws ClassNotFoundException, IOException {
    SurfaceMeshRefinement surfaceMeshRefinement = //
        Serialization.copy(new CatmullClarkRefinement(Se2CoveringBiinvariantMean.INSTANCE));
    SurfaceMesh surfaceMesh = surfaceMeshRefinement.refine(SurfaceMeshExamples.quads6());
    assertEquals(surfaceMesh.faces().size(), 24);
    assertEquals(surfaceMesh.vrt.length(), 35);
  }

  @Test
  void testCube() {
    SurfaceMesh surfaceMesh = PlyFormat.parse(ResourceData.lines("/ch/alpine/sophus/mesh/unitcube.ply"));
    assertTrue(surfaceMesh.boundary().isEmpty());
    SurfaceMeshRefinement surfaceMeshRefinement = new CatmullClarkRefinement(RnBiinvariantMean.INSTANCE);
    SurfaceMesh refine1 = surfaceMeshRefinement.refine(surfaceMesh);
    assertEquals(Flatten.scalars(refine1.vrt).distinct().count(), 7);
    assertEquals(refine1.vrt.length(), 26);
    assertEquals(Mean.of(refine1.vrt), Tensors.vector(0.5, 0.5, 0.5));
    ExactTensorQ.require(refine1.vrt);
    assertEquals(refine1.faces().size(), 24);
    // ---
    SurfaceMesh refine2 = surfaceMeshRefinement.refine(refine1);
    assertEquals(Mean.of(refine2.vrt), Tensors.vector(0.5, 0.5, 0.5));
    ExactTensorQ.require(refine2.vrt);
  }
}

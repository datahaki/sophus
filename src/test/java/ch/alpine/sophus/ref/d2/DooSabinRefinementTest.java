// code by jph
package ch.alpine.sophus.ref.d2;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.lie.rn.RnBiinvariantMean;
import ch.alpine.sophus.srf.SurfaceMesh;
import ch.alpine.sophus.srf.io.PlyFormat;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Flatten;
import ch.alpine.tensor.chq.ExactTensorQ;
import ch.alpine.tensor.io.ResourceData;
import ch.alpine.tensor.red.Mean;

class DooSabinRefinementTest {
  @Test
  void testCube() {
    SurfaceMesh surfaceMesh = PlyFormat.parse(ResourceData.lines("/ch/alpine/sophus/mesh/unitcube.ply"));
    SurfaceMeshRefinement surfaceMeshRefinement = new DooSabinRefinement(RnBiinvariantMean.INSTANCE);
    SurfaceMesh refine1 = surfaceMeshRefinement.refine(surfaceMesh);
    assertEquals(Flatten.scalars(refine1.vrt).distinct().count(), 4); // 0 1/4 3/4 1
    assertEquals(refine1.vrt.length(), 24);
    assertEquals(Mean.of(refine1.vrt), Tensors.vector(0.5, 0.5, 0.5));
    ExactTensorQ.require(refine1.vrt);
    assertEquals(refine1.faces().size(), 6 + 12 + 8);
    // ---
    SurfaceMesh refine2 = surfaceMeshRefinement.refine(refine1);
    assertEquals(Mean.of(refine2.vrt), Tensors.vector(0.5, 0.5, 0.5));
  }
}

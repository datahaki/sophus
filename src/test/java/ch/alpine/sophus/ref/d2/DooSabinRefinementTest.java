// code by jph
package ch.alpine.sophus.ref.d2;

import ch.alpine.sophus.lie.rn.RnBiinvariantMean;
import ch.alpine.sophus.srf.SurfaceMesh;
import ch.alpine.sophus.srf.io.PlyFormat;
import ch.alpine.tensor.ExactTensorQ;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.io.ResourceData;
import ch.alpine.tensor.red.Mean;
import junit.framework.TestCase;

public class DooSabinRefinementTest extends TestCase {
  public void testSimple() {
    SurfaceMesh surfaceMesh = PlyFormat.parse(ResourceData.lines("/io/mesh/unitcube.ply"));
    SurfaceMeshRefinement surfaceMeshRefinement = new DooSabinRefinement(RnBiinvariantMean.INSTANCE);
    SurfaceMesh refine1 = surfaceMeshRefinement.refine(surfaceMesh);
    assertEquals(refine1.vrt.flatten(-1).distinct().count(), 4); // 0 1/4 3/4 1
    assertEquals(refine1.vrt.length(), 24);
    assertEquals(Mean.of(refine1.vrt), Tensors.vector(0.5, 0.5, 0.5));
    ExactTensorQ.require(refine1.vrt);
    assertEquals(refine1.faces().size(), 6 + 12 + 8);
    // ---
    SurfaceMesh refine2 = surfaceMeshRefinement.refine(refine1);
    assertEquals(Mean.of(refine2.vrt), Tensors.vector(0.5, 0.5, 0.5));
  }
}

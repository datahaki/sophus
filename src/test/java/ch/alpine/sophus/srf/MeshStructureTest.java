// code by jph
package ch.alpine.sophus.srf;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.lie.rn.RnBiinvariantMean;
import ch.alpine.sophus.math.IntDirectedEdge;
import ch.alpine.sophus.ref.d2.DooSabinRefinement;
import ch.alpine.sophus.ref.d2.SurfaceMeshExamples;
import ch.alpine.sophus.ref.d2.SurfaceMeshRefinement;
import ch.alpine.sophus.srf.io.PlyFormat;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.io.ResourceData;

class MeshStructureTest {
  @Test
  void testSimple() {
    SurfaceMesh surfaceMesh = PlyFormat.parse(ResourceData.lines("/ch/alpine/sophus/mesh/unitcube.ply"));
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

  @Test
  void testSome() {
    SurfaceMeshRefinement surfaceMeshRefinement = //
        new DooSabinRefinement(RnBiinvariantMean.INSTANCE);
    SurfaceMesh surfaceMesh = SurfaceMeshExamples.mixed7();
    surfaceMeshRefinement.refine(surfaceMesh);
  }

  @Test
  void testSimple2() {
    SurfaceMesh surfaceMesh = SurfaceMeshExamples.mixed7();
    assertEquals(surfaceMesh.vrt.get(3), Tensors.vector(2, 2, 0));
    assertEquals(Tensors.vectorInt(surfaceMesh.face(0)), Tensors.vectorInt(0, 2, 3, 1));
    // surfaceMeshRefinement.refine(surfaceMesh);
    MeshStructure meshStructure = new MeshStructure(surfaceMesh);
    List<IntDirectedEdge> list = meshStructure.ring(new IntDirectedEdge(3, 1));
    assertEquals(list.stream().distinct().count(), list.size());
  }
}

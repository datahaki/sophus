// code by jph
package ch.alpine.sophus.ref.d2;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import ch.alpine.sophus.bm.BiinvariantMean;
import ch.alpine.sophus.math.IntDirectedEdge;
import ch.alpine.sophus.math.win.UniformWindowSampler;
import ch.alpine.sophus.srf.SurfaceMesh;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.ext.Integers;
import ch.alpine.tensor.sca.win.DirichletWindow;

public record TriQuadLinearRefinement(BiinvariantMean biinvariantMean) implements SurfaceMeshRefinement, Serializable {
  private static final Function<Integer, Tensor> WEIGHTS = UniformWindowSampler.of(DirichletWindow.FUNCTION);

  @Override // from SurfaceMeshRefinement
  public SurfaceMesh refine(SurfaceMesh surfaceMesh) {
    SurfaceMesh out = new SurfaceMesh();
    // insert all vertices into output mesh
    out.vrt = surfaceMesh.vrt.copy(); // interpolation
    // insert all face-midpoints into output mesh
    Map<IntDirectedEdge, Integer> intDirectedEdges = new HashMap<>();
    for (int[] face : surfaceMesh.faces()) {
      int n = face.length;
      List<Integer> list = new ArrayList<>(); // index of edge midpoints
      for (int index = 0; index < n; ++index) {
        IntDirectedEdge intDirectedEdge = new IntDirectedEdge( //
            face[index], //
            face[(index + 1) % n]);
        if (intDirectedEdges.containsKey(intDirectedEdge)) // edge already was subdivided
          list.add(intDirectedEdges.get(intDirectedEdge));
        else {
          Tensor sequence = surfaceMesh.polygon_face(intDirectedEdge.array());
          Tensor midpoint = biinvariantMean.mean(sequence, WEIGHTS.apply(sequence.length()));
          int v_index = out.addVert(midpoint);
          intDirectedEdges.put(intDirectedEdge.reverse(), v_index);
          list.add(v_index);
        }
      }
      // here, list contains face.length entries
      Integers.requireEquals(list.size(), n);
      if (3 == n) {
        for (int index = 0; index < n; ++index)
          out.addFace( //
              face[index], //
              list.get(index), //
              list.get(Math.floorMod(index - 1, n)));
        out.addFace( //
            list.get(0), list.get(1), list.get(2));
      } else
        if (3 < n) {
          // add quad consisting of old face vertex, two edge midpoints, and the face midpoint
          int nV = out.addVert(biinvariantMean.mean(surfaceMesh.polygon_face(face), WEIGHTS.apply(n)));
          for (int index = 0; index < n; ++index)
            out.addFace( //
                face[index], //
                list.get(index), //
                nV, //
                list.get(Math.floorMod(index - 1, n)));
        }
    }
    return out;
  }
}

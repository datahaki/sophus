// code by jph
package ch.alpine.sophus.ref.d2;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

import ch.alpine.sophus.bm.BiinvariantMean;
import ch.alpine.sophus.math.win.UniformWindowSampler;
import ch.alpine.sophus.srf.SurfaceMesh;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.sca.win.DirichletWindow;

public record LinearSurfaceMeshRefinement(BiinvariantMean biinvariantMean) implements SurfaceMeshRefinement, Serializable {
  private static final Function<Integer, Tensor> WEIGHTS = UniformWindowSampler.of(DirichletWindow.FUNCTION);

  public LinearSurfaceMeshRefinement {
    Objects.requireNonNull(biinvariantMean);
  }

  @Override // from SurfaceMeshRefinement
  public SurfaceMesh refine(SurfaceMesh surfaceMesh) {
    SurfaceMesh out = new SurfaceMesh();
    // insert all vertices into output mesh
    out.vrt = surfaceMesh.vrt.copy(); // interpolation
    int nV = surfaceMesh.vrt.length();
    // insert all face-midpoints into output mesh
    for (int[] face : surfaceMesh.faces()) {
      Tensor sequence = surfaceMesh.polygon_face(face);
      Tensor midpoint = biinvariantMean.mean(sequence, WEIGHTS.apply(sequence.length()));
      out.addVert(midpoint);
    }
    Map<Edge, Integer> edges = new HashMap<>();
    for (int[] face : surfaceMesh.faces()) {
      List<Integer> list = new ArrayList<>(); // index of edge midpoints
      for (int index = 0; index < face.length; ++index) {
        Edge edge = new Edge( //
            face[index], //
            face[(index + 1) % face.length]);
        if (edges.containsKey(edge)) // edge already was subdivided
          list.add(edges.get(edge));
        else {
          Tensor sequence = surfaceMesh.polygon_face(edge.array());
          Tensor midpoint = biinvariantMean.mean(sequence, WEIGHTS.apply(sequence.length()));
          int v_index = out.addVert(midpoint);
          edges.put(edge.reverse(), v_index);
          list.add(v_index);
        }
      }
      // here, list contains face.length entries
      // Integers.requireEquals(list.size(), face.length);
      // add quad consisting of old face vertex, two edge midpoints, and the face midpoint
      for (int index = 0; index < face.length; ++index)
        out.addFace( //
            face[index], //
            list.get(index), //
            nV, //
            list.get(Math.floorMod(index - 1, face.length)));
      ++nV;
    }
    return out;
  }
}

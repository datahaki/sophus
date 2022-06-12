// code by jph
package ch.alpine.sophus.srf;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import ch.alpine.sophus.math.IntDirectedEdge;

public class MeshStructure {
  private static final int MAX_ITERATIONS = 50;
  // ---
  private final SurfaceMesh surfaceMesh;
  private final Map<IntDirectedEdge, IntDirectedEdge> edge_face = new HashMap<>();

  public MeshStructure(SurfaceMesh surfaceMesh) {
    this.surfaceMesh = surfaceMesh;
    // ---
    int find = 0;
    for (int[] face : surfaceMesh.faces()) {
      int n = face.length;
      for (int index = 0; index < n; ++index)
        edge_face.put( //
            new IntDirectedEdge(face[index], face[(index + 1) % n]), //
            new IntDirectedEdge(find, index));
      ++find;
    }
  }

  public List<IntDirectedEdge> ring(IntDirectedEdge seed) {
    List<IntDirectedEdge> list = new LinkedList<>();
    list.add(seed);
    int iteration = 0;
    IntDirectedEdge next = seed;
    while (iteration < MAX_ITERATIONS) {
      IntDirectedEdge nfac = edge_face.get(next);
      if (Objects.isNull(nfac))
        return List.of();
      int[] fce = surfaceMesh.face(nfac.i());
      next = new IntDirectedEdge(fce[nfac.j()], fce[(nfac.j() + fce.length - 1) % fce.length]);
      if (next.equals(seed))
        return list;
      list.add(next);
      ++iteration;
    }
    return List.of();
  }
}

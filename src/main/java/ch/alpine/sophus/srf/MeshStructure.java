// code by jph
package ch.alpine.sophus.srf;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import ch.alpine.sophus.math.DirectedEdge;

public class MeshStructure {
  private final SurfaceMesh surfaceMesh;
  private final Map<DirectedEdge, DirectedEdge> edge_face = new HashMap<>();

  public MeshStructure(SurfaceMesh surfaceMesh) {
    this.surfaceMesh = surfaceMesh;
    // ---
    int find = 0;
    for (int[] face : surfaceMesh.faces()) {
      int n = face.length;
      for (int index = 0; index < n; ++index)
        edge_face.put( //
            new DirectedEdge(face[index], face[(index + 1) % n]), //
            new DirectedEdge(find, index));
      ++find;
    }
  }

  public List<DirectedEdge> ring(DirectedEdge seed) {
    List<DirectedEdge> list = new LinkedList<>();
    list.add(seed);
    int count = 0;
    DirectedEdge next = seed;
    while (count < 10) {
      DirectedEdge nfac = edge_face.get(next);
      if (Objects.isNull(nfac))
        return Arrays.asList();
      int[] fce = surfaceMesh.face(nfac.i());
      next = new DirectedEdge(fce[nfac.j()], fce[(nfac.j() + fce.length - 1) % fce.length]);
      if (next.equals(seed))
        return list;
      list.add(next);
      ++count;
    }
    return Arrays.asList();
  }
}

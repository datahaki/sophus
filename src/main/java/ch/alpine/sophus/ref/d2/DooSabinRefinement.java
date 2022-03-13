// code by jph
package ch.alpine.sophus.ref.d2;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.stream.IntStream;

import ch.alpine.sophus.bm.BiinvariantMean;
import ch.alpine.sophus.math.DirectedEdge;
import ch.alpine.sophus.srf.MeshStructure;
import ch.alpine.sophus.srf.SurfaceMesh;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.RotateRight;

/** Reference:
 * "Behaviour of recursive division surfaces near extraordinary points"
 * by D. Doo, M. Sabin, Computer-Aided Design 10(6), 1978 */
public record DooSabinRefinement(BiinvariantMean biinvariantMean) implements SurfaceMeshRefinement, Serializable {
  public DooSabinRefinement {
    Objects.requireNonNull(biinvariantMean);
  }

  @Override // from SurfaceMeshRefinement
  public SurfaceMesh refine(SurfaceMesh surfaceMesh) {
    SurfaceMesh out = new SurfaceMesh();
    MeshStructure meshStructure = new MeshStructure(surfaceMesh);
    Map<DirectedEdge, DirectedEdge> edge_outVrt = new HashMap<>();
    for (int[] face : surfaceMesh.faces()) {
      int n = face.length;
      Tensor sequence = Tensor.of(Arrays.stream(face).mapToObj(surfaceMesh.vrt::get));
      Tensor weights = DooSabinWeights.CACHE.apply(n);
      int ofs = out.vrt.length();
      for (int index = 0; index < n; ++index) {
        out.addVert(biinvariantMean.mean(sequence, RotateRight.of(weights, index)));
        DirectedEdge directedEdge = new DirectedEdge(face[index], face[(index + 1) % n]);
        edge_outVrt.put(directedEdge, new DirectedEdge(ofs + index, ofs + ((index + 1) % n)));
      }
      out.addFace(IntStream.range(ofs, ofs + n).toArray());
    }
    // at this point out.vrt is complete
    // for each edge in given surface mesh we add a quad
    {
      Set<DirectedEdge> set = new HashSet<>();
      for (Entry<DirectedEdge, DirectedEdge> entry : edge_outVrt.entrySet()) {
        DirectedEdge edge1 = entry.getKey();
        if (!set.contains(edge1)) {
          DirectedEdge edge2 = edge1.reverse();
          DirectedEdge res2 = edge_outVrt.get(edge2);
          if (Objects.nonNull(res2)) {
            DirectedEdge res1 = entry.getValue();
            out.addFace(new int[] { res1.j(), res1.i(), res2.j(), res2.i() });
            set.add(edge2);
          }
        }
      }
    }
    // for each vertex in given surface mesh we add a ring
    {
      Set<Integer> set = new HashSet<>();
      for (int[] face : surfaceMesh.faces()) {
        int n = face.length;
        for (int index = 0; index < n; ++index) {
          int i = face[index];
          if (!set.contains(i)) {
            set.add(i);
            // ---
            final DirectedEdge seed = new DirectedEdge(face[index], face[(index + 1) % n]);
            List<DirectedEdge> ring = meshStructure.ring(seed);
            if (!ring.isEmpty()) {
              int[] frng = new int[ring.size()];
              for (int in = 0; in < frng.length; ++in)
                frng[in] = edge_outVrt.get(ring.get(in)).i();
              out.addFace(frng);
            }
          }
        }
      }
    }
    return out;
  }
}

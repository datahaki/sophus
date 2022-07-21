// code by jph
package ch.alpine.sophus.ref.d2;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import ch.alpine.sophus.bm.BiinvariantMean;
import ch.alpine.sophus.math.IntDirectedEdge;
import ch.alpine.sophus.srf.SurfaceMesh;
import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;

/** implementation using linear averaging and smoothing
 * but without correcting term, i.e. without zipping
 * 
 * References:
 * "Recursively generated B-spline surfaces on arbitrary topological meshes"
 * by Catmull, Clark; Computer-Aided Design 16(6), 1978
 * 
 * "A Factored Approach to Subdivision Surfaces"
 * by Joe Warren, Scott Schaefer, 2003 */
public record CatmullClarkRefinement(BiinvariantMean biinvariantMean) //
    implements SurfaceMeshRefinement, Serializable {
  @Override // from SurfaceMeshRefinement
  public SurfaceMesh refine(SurfaceMesh surfaceMesh) {
    SurfaceMeshRefinement surfaceMeshRefinement = new QuadLinearRefinement(biinvariantMean);
    SurfaceMesh out = surfaceMeshRefinement.refine(surfaceMesh);
    int index = 0;
    Tensor cpy = Tensors.reserve(out.vrt.length());
    Set<Integer> boundary = out.boundary();
    for (List<IntDirectedEdge> list : out.vertToFace()) {
      int n = list.size();
      if (boundary.contains(index))
        cpy.append(out.vrt.get(index));
      else {
        Tensor sequence = Tensors.reserve(2 * n + 1);
        Tensor weights = Tensors.reserve(2 * n + 1);
        // weights are from "Figure 7" in Warren/Schaefer
        Scalar ga = RationalScalar.of(1, 4);
        Scalar al = RationalScalar.of(1, 4L * n);
        Scalar be = RationalScalar.of(1, 2L * n);
        for (IntDirectedEdge intDirectedEdge : list) {
          sequence.append(out.vrt.get(out.face(intDirectedEdge.i())[(intDirectedEdge.j() + 1) % 4]));
          sequence.append(out.vrt.get(out.face(intDirectedEdge.i())[(intDirectedEdge.j() + 2) % 4]));
          weights.append(be);
          weights.append(al);
        }
        sequence.append(out.vrt.get(index));
        weights.append(ga);
        cpy.append(biinvariantMean.mean(sequence, weights));
      }
      ++index;
    }
    out.vrt = cpy;
    return out;
  }
}

// code by jph
package ch.alpine.sophus.ref.d2;

import java.io.Serializable;
import java.util.List;

import ch.alpine.sophus.bm.BiinvariantMean;
import ch.alpine.sophus.math.IntDirectedEdge;
import ch.alpine.sophus.srf.SurfaceMesh;
import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;

/** implementation using linear averaging and smoothing
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
    LinearSurfaceMeshRefinement linearSurfaceMeshRefinement = new LinearSurfaceMeshRefinement(biinvariantMean);
    SurfaceMesh out = linearSurfaceMeshRefinement.refine(surfaceMesh);
    int vix = 0;
    Tensor cpy = Tensors.reserve(out.vrt.length());
    for (List<IntDirectedEdge> list : out.vertToFace()) {
      int n = list.size();
      // FIXME SOPHUS SUB identify boundary criteria 2<n leads to mixed behaviour along boundary
      if (2 < n) {
        Tensor sequence = Tensors.reserve(2 * n + 1);
        Tensor weights = Tensors.reserve(2 * n + 1);
        // weights are from "Figure 7" in Warren/Schaefer
        Scalar ga = RationalScalar.of(1, 4);
        Scalar al = RationalScalar.of(1, 4 * n);
        Scalar be = RationalScalar.of(1, 2 * n);
        for (IntDirectedEdge fix : list) {
          sequence.append(out.vrt.get(out.face(fix.i())[(fix.j() + 1) % 4]));
          sequence.append(out.vrt.get(out.face(fix.i())[(fix.j() + 2) % 4]));
          weights.append(be);
          weights.append(al);
        }
        sequence.append(out.vrt.get(vix));
        weights.append(ga);
        cpy.append(biinvariantMean.mean(sequence, weights));
      } else
        cpy.append(out.vrt.get(vix));
      ++vix;
    }
    out.vrt = cpy;
    return out;
  }
}

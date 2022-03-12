// code by jph
package ch.alpine.sophus.ref.d2;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import ch.alpine.sophus.bm.BiinvariantMean;
import ch.alpine.sophus.srf.SurfaceMesh;
import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.red.FirstPosition;

/** Reference:
 * "Recursively generated B-spline surfaces on arbitrary topological meshes"
 * by Catmull, Clark; Computer-Aided Design 16(6), 1978 */
public class CatmullClarkRefinement implements SurfaceMeshRefinement, Serializable {
  /** @param biinvariantMean non-null
   * @return */
  public static SurfaceMeshRefinement of(BiinvariantMean biinvariantMean) {
    return new CatmullClarkRefinement(Objects.requireNonNull(biinvariantMean));
  }

  // ---
  private final BiinvariantMean biinvariantMean;
  private final SurfaceMeshRefinement surfaceMeshRefinement;

  private CatmullClarkRefinement(BiinvariantMean biinvariantMean) {
    this.biinvariantMean = biinvariantMean;
    surfaceMeshRefinement = new LinearSurfaceMeshRefinement(biinvariantMean);
  }

  @Override // from SurfaceMeshRefinement
  public SurfaceMesh refine(SurfaceMesh surfaceMesh) {
    // TODO SOPHUS ALG big assumption: is decomposition sub=lin+avg still possible?
    SurfaceMesh out = surfaceMeshRefinement.refine(surfaceMesh);
    int vix = 0;
    Tensor cpy = out.vrt.copy();
    for (List<Integer> list : out.vertToFace()) {
      int n = list.size();
      if (2 < n) {
        // TODO SOPHUS SUB identify boundary
        Tensor sequence = Tensors.reserve(2 * n + 1);
        Tensor weights = Tensors.reserve(2 * n + 1);
        Scalar ga = RationalScalar.of(1, 4);
        Scalar al = RationalScalar.of(1, 4 * n);
        Scalar be = RationalScalar.of(1, 2 * n);
        Scalar elem = RealScalar.of(vix);
        for (int fix : list) { // edges from vertex ring (unordered)
          int pos = FirstPosition.of(Tensors.vectorInt(out.face(fix)), elem).getAsInt();
          int p1 = out.face(fix)[(pos + 1) % 4];
          int p2 = out.face(fix)[(pos + 2) % 4];
          sequence.append(out.vrt.get(p1));
          sequence.append(out.vrt.get(p2));
          weights.append(be);
          weights.append(al);
        }
        Tensor interp = out.vrt.get(vix);
        sequence.append(interp);
        weights.append(ga);
        cpy.set(biinvariantMean.mean(sequence, weights), vix);
      }
      ++vix;
    }
    out.vrt = cpy;
    return out;
  }
}

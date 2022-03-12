// code by jph
package ch.alpine.sophus.ref.d2;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.IntStream;

import ch.alpine.sophus.bm.BiinvariantMean;
import ch.alpine.sophus.srf.SurfaceMesh;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.RotateLeft;

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
    for (int[] face : surfaceMesh.faces()) {
      Tensor sequence = Tensor.of(Arrays.stream(face).mapToObj(surfaceMesh.vrt::get));
      int n = sequence.length();
      Tensor weights = DooSabinWeights.CACHE.apply(n);
      int ofs = out.vrt.length();
      for (int offset = 0; offset < n; ++offset)
        out.addVert(biinvariantMean.mean(sequence, RotateLeft.of(weights, offset)));
      out.addFace(IntStream.range(ofs, ofs + n).toArray());
    }
    // at this point out.vrt is complete
    // for each edge in given surface mesh we have to add a quad
    // for each vertex in given surface mesh we have to add a ring
    // TODO SOPHUS SUB add quads at vertices and edges
    return out;
  }
}

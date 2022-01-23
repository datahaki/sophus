// code by jph
package ch.alpine.sophus.ref.d2;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

import ch.alpine.sophus.bm.BiinvariantMean;
import ch.alpine.sophus.math.win.UniformWindowSampler;
import ch.alpine.sophus.srf.SurfaceMesh;
import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.io.Primitives;
import ch.alpine.tensor.sca.win.DirichletWindow;

public record LinearMeshRefinement(BiinvariantMean biinvariantMean) implements SurfaceMeshRefinement, Serializable {
  private static final Tensor MIDPOINT = Tensors.of(RationalScalar.HALF, RationalScalar.HALF);
  private static final Function<Integer, Tensor> WEIGHTS = UniformWindowSampler.of(DirichletWindow.FUNCTION);

  public LinearMeshRefinement {
    Objects.requireNonNull(biinvariantMean);
  }

  @Override // from SurfaceMeshRefinement
  public SurfaceMesh refine(SurfaceMesh surfaceMesh) {
    SurfaceMesh out = new SurfaceMesh();
    out.vrt = surfaceMesh.vrt.copy(); // interpolation
    int nV = surfaceMesh.vrt.length();
    for (Tensor face : surfaceMesh.ind) { // midpoint
      Tensor sequence = Tensor.of(Arrays.stream(Primitives.toIntArray(face)).mapToObj(surfaceMesh.vrt::get));
      out.addVert(biinvariantMean.mean(sequence, WEIGHTS.apply(sequence.length())));
    }
    Map<Tensor, Integer> edges = new HashMap<>();
    int faceInd = nV;
    for (Tensor face : surfaceMesh.ind) {
      List<Integer> list = new ArrayList<>();
      for (int c0 = 0; c0 < face.length(); ++c0) {
        Scalar p0 = face.Get(c0);
        Scalar p1 = face.Get((c0 + 1) % face.length());
        Tensor key = Tensors.of(p0, p1);
        if (edges.containsKey(key))
          list.add(edges.get(key));
        else {
          Tensor sequence = Tensors.of( //
              surfaceMesh.vrt.get(p0.number().intValue()), //
              surfaceMesh.vrt.get(p1.number().intValue()));
          Tensor mid = biinvariantMean.mean(sequence, MIDPOINT);
          int index = out.addVert(mid);
          edges.put(Tensors.of(p1, p0), index);
          list.add(index);
        }
      }
      for (int c0 = 0; c0 < face.length(); ++c0) {
        Scalar p0 = face.Get(c0);
        out.addFace( //
            p0.number().intValue(), //
            list.get(c0), //
            faceInd, //
            list.get(Math.floorMod(c0 - 1, face.length())));
      }
      ++faceInd;
    }
    return out;
  }
}

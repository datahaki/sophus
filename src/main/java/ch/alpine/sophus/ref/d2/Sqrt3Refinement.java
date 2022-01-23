// code by jph
package ch.alpine.sophus.ref.d2;

import ch.alpine.sophus.bm.BiinvariantMean;
import ch.alpine.sophus.srf.SurfaceMesh;
import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.ConstantArray;

public record Sqrt3Refinement(BiinvariantMean biinvariantMean) implements SurfaceMeshRefinement {
  private static final Tensor WEIGHTS = ConstantArray.of(RationalScalar.of(1, 3), 3);

  @Override
  public SurfaceMesh refine(SurfaceMesh surfaceMesh) {
    SurfaceMesh out = new SurfaceMesh();
    for (Tensor tri : surfaceMesh.ind) {
      Tensor sequence = Tensors.of( //
          surfaceMesh.vrt.get(tri.Get(0).number().intValue()), //
          surfaceMesh.vrt.get(tri.Get(1).number().intValue()), //
          surfaceMesh.vrt.get(tri.Get(2).number().intValue()));
      Tensor mean = biinvariantMean.mean(sequence, WEIGHTS);
      out.addVert(mean);
    }
    return out;
  }
}

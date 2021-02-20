// code by jph
package ch.ethz.idsc.sophus.ref.d2;

import ch.ethz.idsc.sophus.bm.BiinvariantMean;
import ch.ethz.idsc.sophus.srf.SurfaceMesh;
import ch.ethz.idsc.tensor.RationalScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.ConstantArray;

public class Sqrt3Refinement implements SurfaceMeshRefinement {
  private static final Tensor WEIGHTS = ConstantArray.of(RationalScalar.of(1, 3), 3);
  // ---
  private final BiinvariantMean biinvariantMean;

  public Sqrt3Refinement(BiinvariantMean biinvariantMean) {
    this.biinvariantMean = biinvariantMean;
  }

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

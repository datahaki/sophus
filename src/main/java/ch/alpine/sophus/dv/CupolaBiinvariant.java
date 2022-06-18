// code by jph
package ch.alpine.sophus.dv;

import ch.alpine.sophus.hs.Manifold;
import ch.alpine.sophus.hs.Sedarim;
import ch.alpine.sophus.hs.gr.GrManifold;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.ScalarUnaryOperator;

/** bi-invariant
 * results in a symmetric distance matrix -> can use for kriging and minimum spanning tree */
/* package */ class CupolaBiinvariant extends BiinvariantBase {
  public CupolaBiinvariant(Manifold manifold) {
    super(manifold);
  }

  @Override // from Biinvariant
  public Sedarim distances(Tensor sequence) {
    BiinvariantVectorFunction biinvariantVectorFunction = //
        new InfluenceBiinvariantVector(hsDesign(), sequence, GrManifold.INSTANCE);
    return point -> biinvariantVectorFunction.biinvariantVector(point).vector();
  }

  @Override // from Biinvariant
  public Sedarim coordinate(ScalarUnaryOperator variogram, Tensor sequence) {
    return new BiinvariantVectorCoordinate(CupolaBiinvariantVector.of(hsDesign(), sequence), variogram);
  }
}

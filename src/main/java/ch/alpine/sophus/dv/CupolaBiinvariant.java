// code by jph
package ch.alpine.sophus.dv;

import ch.alpine.sophus.gbc.BiinvariantVectorCoordinate;
import ch.alpine.sophus.hs.Manifold;
import ch.alpine.sophus.hs.gr.GrMetric;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.ScalarUnaryOperator;
import ch.alpine.tensor.api.TensorUnaryOperator;

/** bi-invariant
 * results in a symmetric distance matrix -> can use for kriging and minimum spanning tree */
public class CupolaBiinvariant extends BiinvariantBase {
  public CupolaBiinvariant(Manifold manifold) {
    super(manifold);
  }

  @Override // from Biinvariant
  public TensorUnaryOperator distances(Tensor sequence) {
    BiinvariantVectorFunction biinvariantVectorFunction = //
        new InfluenceBiinvariantVector(hsDesign(), sequence, GrMetric.INSTANCE);
    return point -> biinvariantVectorFunction.biinvariantVector(point).vector();
  }

  @Override // from Biinvariant
  public TensorUnaryOperator coordinate(ScalarUnaryOperator variogram, Tensor sequence) {
    return new BiinvariantVectorCoordinate(CupolaBiinvariantVector.of(hsDesign(), sequence), variogram);
  }
}

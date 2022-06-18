// code by jph
package ch.alpine.sophus.hs;

import ch.alpine.sophus.dv.CupolaBiinvariantVector;
import ch.alpine.sophus.gbc.CupolaCoordinate;
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
        CupolaBiinvariantVector.of(manifold, sequence);
    return point -> biinvariantVectorFunction.biinvariantVector(point).vector();
  }

  @Override // from Biinvariant
  public TensorUnaryOperator coordinate(ScalarUnaryOperator variogram, Tensor sequence) {
    return CupolaCoordinate.of(manifold, variogram, sequence);
  }
}

// code by jph
package ch.alpine.sophus.dv;

import ch.alpine.sophus.gbc.HarborCoordinate;
import ch.alpine.sophus.hs.Manifold;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.ScalarUnaryOperator;
import ch.alpine.tensor.api.TensorUnaryOperator;

/** bi-invariant
 * results in a symmetric distance matrix -> can use for kriging and minimum spanning tree */
public class HarborBiinvariant extends BiinvariantBase {
  public HarborBiinvariant(Manifold manifold) {
    super(manifold);
  }

  @Override // from Biinvariant
  public TensorUnaryOperator distances(Tensor sequence) {
    BiinvariantVectorFunction biinvariantVectorFunction = HarborBiinvariantVector.of(hsDesign(), sequence);
    return point -> biinvariantVectorFunction.biinvariantVector(point).vector();
  }

  @Override // from Biinvariant
  public TensorUnaryOperator coordinate(ScalarUnaryOperator variogram, Tensor sequence) {
    return HarborCoordinate.of(hsDesign(), variogram, sequence);
  }
}

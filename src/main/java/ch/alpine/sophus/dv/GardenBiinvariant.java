// code by jph
package ch.alpine.sophus.dv;

import ch.alpine.sophus.gbc.GardenCoordinate;
import ch.alpine.sophus.hs.Manifold;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.ScalarUnaryOperator;
import ch.alpine.tensor.api.TensorUnaryOperator;

/** bi-invariant
 * results in a symmetric distance matrix -> can use for kriging */
public class GardenBiinvariant extends BiinvariantBase {
  public GardenBiinvariant(Manifold manifold) {
    super(manifold);
  }

  @Override // from Biinvariant
  public TensorUnaryOperator distances(Tensor sequence) {
    return GardenDistanceVector.of(manifold, sequence);
  }

  @Override // from Biinvariant
  public TensorUnaryOperator coordinate(ScalarUnaryOperator variogram, Tensor sequence) {
    return GardenCoordinate.of(manifold, variogram, sequence);
  }
}

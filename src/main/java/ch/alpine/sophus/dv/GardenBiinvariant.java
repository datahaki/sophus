// code by jph
package ch.alpine.sophus.dv;

import ch.alpine.sophus.hs.Manifold;
import ch.alpine.sophus.hs.Sedarim;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.ScalarUnaryOperator;

/** bi-invariant
 * results in a symmetric distance matrix -> can use for kriging */
/* package */ class GardenBiinvariant extends BiinvariantBase {
  public GardenBiinvariant(Manifold manifold) {
    super(manifold);
  }

  @Override // from Biinvariant
  public Sedarim distances(Tensor sequence) {
    return new GardenDistanceVector(manifold, sequence);
  }

  @Override // from Biinvariant
  public Sedarim coordinate(ScalarUnaryOperator variogram, Tensor sequence) {
    return new GardenCoordinate(manifold, variogram, sequence);
  }
}

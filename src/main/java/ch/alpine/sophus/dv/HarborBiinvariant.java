// code by jph
package ch.alpine.sophus.dv;

import ch.alpine.sophus.hs.Manifold;
import ch.alpine.sophus.hs.Sedarim;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.ScalarUnaryOperator;

/** bi-invariant
 * results in a symmetric distance matrix -> can use for kriging and minimum spanning tree */
public class HarborBiinvariant extends BiinvariantBase {
  public HarborBiinvariant(Manifold manifold) {
    super(manifold);
  }

  @Override // from Biinvariant
  public Sedarim distances(Tensor sequence) {
    BiinvariantVectorFunction biinvariantVectorFunction = HarborBiinvariantVector.of(hsDesign(), sequence);
    return point -> biinvariantVectorFunction.biinvariantVector(point).vector();
  }

  @Override // from Biinvariant
  public Sedarim coordinate(ScalarUnaryOperator variogram, Tensor sequence) {
    return new BiinvariantVectorCoordinate(HarborBiinvariantVector.of(hsDesign(), sequence), variogram);
  }
}

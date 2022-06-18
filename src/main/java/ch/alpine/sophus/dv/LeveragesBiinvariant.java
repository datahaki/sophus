// code by jph
package ch.alpine.sophus.dv;

import java.util.Objects;

import ch.alpine.sophus.hs.HsGenesis;
import ch.alpine.sophus.hs.Manifold;
import ch.alpine.sophus.hs.Sedarim;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.ScalarUnaryOperator;
import ch.alpine.tensor.nrm.NormalizeTotal;

/** bi-invariant
 * does not result in a symmetric distance matrix -> should not use for kriging
 * 
 * Reference:
 * "Biinvariant Generalized Barycentric Coordinates on Lie Groups"
 * by Jan Hakenberg, 2020 */
/* package */ class LeveragesBiinvariant extends BiinvariantBase {
  public LeveragesBiinvariant(Manifold manifold) {
    super(manifold);
  }

  @Override // from Biinvariant
  public Sedarim distances(Tensor sequence) {
    return HsGenesis.wrap(hsDesign(), LeveragesDistanceVector.INSTANCE, sequence);
  }

  @Override // from Biinvariant
  public Sedarim coordinate(ScalarUnaryOperator variogram, Tensor sequence) {
    return HsGenesis.wrap(hsDesign(), new LeveragesGenesis(variogram), sequence);
  }

  @Override // from Biinvariant
  public Sedarim lagrainate(ScalarUnaryOperator variogram, Tensor sequence) {
    Objects.requireNonNull(variogram);
    Objects.requireNonNull(sequence);
    return point -> {
      Tensor levers = Tensor.of(sequence.stream().map(manifold.exponential(point)::vectorLog));
      Tensor target = NormalizeTotal.FUNCTION.apply(LeveragesDistanceVector.INSTANCE.origin(levers).map(variogram));
      return LagrangeCoordinates.of(levers, target);
    };
  }
}

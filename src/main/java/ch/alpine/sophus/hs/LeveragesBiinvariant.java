// code by jph
package ch.alpine.sophus.hs;

import java.util.Objects;

import ch.alpine.sophus.dv.LeveragesDistanceVector;
import ch.alpine.sophus.gbc.LagrangeCoordinates;
import ch.alpine.sophus.gbc.LeveragesGenesis;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.ScalarUnaryOperator;
import ch.alpine.tensor.api.TensorUnaryOperator;
import ch.alpine.tensor.nrm.NormalizeTotal;

/** bi-invariant
 * does not result in a symmetric distance matrix -> should not use for kriging
 * 
 * Reference:
 * "Biinvariant Generalized Barycentric Coordinates on Lie Groups"
 * by Jan Hakenberg, 2020 */
public class LeveragesBiinvariant extends BiinvariantBase {
  public LeveragesBiinvariant(Manifold manifold) {
    super(manifold);
  }

  @Override // from Biinvariant
  public TensorUnaryOperator distances(Tensor sequence) {
    return HsGenesis.wrap(hsDesign, LeveragesDistanceVector.INSTANCE, sequence);
  }

  @Override // from Biinvariant
  public TensorUnaryOperator coordinate(ScalarUnaryOperator variogram, Tensor sequence) {
    return HsGenesis.wrap(hsDesign, new LeveragesGenesis(variogram), sequence);
  }

  @Override // from Biinvariant
  public TensorUnaryOperator lagrainate(ScalarUnaryOperator variogram, Tensor sequence) {
    Objects.requireNonNull(manifold);
    Objects.requireNonNull(variogram);
    Objects.requireNonNull(sequence);
    return point -> {
      Tensor levers = Tensor.of(sequence.stream().map(manifold.exponential(point)::vectorLog));
      Tensor target = NormalizeTotal.FUNCTION.apply(LeveragesDistanceVector.INSTANCE.origin(levers).map(variogram));
      return LagrangeCoordinates.of(levers, target);
    };
  }
}

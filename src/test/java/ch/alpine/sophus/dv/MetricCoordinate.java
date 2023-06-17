// code by jph
package ch.alpine.sophus.dv;

import java.util.Objects;

import ch.alpine.sophus.hs.Genesis;
import ch.alpine.sophus.lie.rn.RnGroup;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.ScalarUnaryOperator;

/** ONLY FOR TESTING */
public record MetricCoordinate(Genesis genesis) implements Genesis {

  /** Careful:
   * Distance may depend on sequence! In that case only the correct sequence
   * should be passed to the function {@link #weights(Tensor, Tensor)}!
   * 
   * @param variogram for example InversePowerVariogram.of(2)
   * @return genesis operator with design matrix as input */
  public static Genesis of(ScalarUnaryOperator variogram) {
    return new MetricBiinvariant(RnGroup.INSTANCE).coordinate(variogram);
  }
  private static final Genesis AFFINE = new MetricCoordinate(AveragingWeights.INSTANCE);

  /** Affine coordinates are a special case of inverse distance coordinates
   * namely with exponent beta == 0.
   * 
   * Affine coordinates can be obtained by solving a linear system.
   * 
   * Reference:
   * "Affine generalised barycentric coordinates"
   * by S. Waldron, Jaen Journal on Approximation, 3(2):209-226, 2011
   * 
   * @return
   * @see AffineCoordinate */
  public static Genesis affine() {
    return AFFINE;
  }

  public MetricCoordinate {
    Objects.requireNonNull(genesis);
  }

  @Override // from Genesis
  public Tensor origin(Tensor levers) {
    return StaticHelper.barycentric(levers, genesis.origin(levers));
  }
}

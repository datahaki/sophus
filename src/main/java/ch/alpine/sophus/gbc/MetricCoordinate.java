// code by jph
package ch.alpine.sophus.gbc;

import java.io.Serializable;
import java.util.Objects;

import ch.alpine.sophus.api.Genesis;
import ch.alpine.sophus.itp.InverseDistanceWeighting;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.ScalarUnaryOperator;

/** partition of unity
 * linear reproduction
 * Lagrange
 * C^infinity (except at points from input set)
 * 
 * in general, the coordinates may evaluate to be negative
 * 
 * Lie affine coordinates are generalized barycentric coordinates for
 * scattered sets of points on a Lie-group with the properties:
 * 
 * coordinates sum up to 1
 * linear reproduction
 * Biinvariant: invariant under left-, right- and inverse action
 * 
 * However, generally NOT fulfilled:
 * Lagrange property
 * non-negativity
 * 
 * Log[g.m.g^-1] == Ad[g].Log[m]
 * Log[g.m] == Ad[g].Log[m.g]
 * Log[g^-1.m] == Ad[g^-1].Log[m.g^-1]
 * Ad[g].Log[g^-1.m] == Log[m.g^-1]
 * 
 * invariance under left-action is guaranteed because
 * log [(g x)^-1 g p] == log [x^-1 p]
 * 
 * If the target mapping is Ad invariant then invariance under right action
 * and inversion is guaranteed.
 * 
 * If the target mapping correlates to inverse distances then the coordinates
 * satisfy the Lagrange property.
 * 
 * References:
 * "Inverse Distance Coordinates for Scattered Sets of Points"
 * by Jan Hakenberg, 2020
 * 
 * "Biinvariant Generalized Barycentric Coordinates on Lie Groups"
 * by Jan Hakenberg, 2020 */
public record MetricCoordinate(Genesis genesis) implements Genesis, Serializable {
  /** Careful:
   * Distance may depend on sequence! In that case only the correct sequence
   * should be passed to the function {@link #weights(Tensor, Tensor)}!
   * 
   * @param genesis operator with design matrix as input
   * @return */
  /** @param variogram for example InversePowerVariogram.of(2)
   * @return */
  public static Genesis of(ScalarUnaryOperator variogram) {
    return new MetricCoordinate(InverseDistanceWeighting.of(variogram));
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

  @Override // from BarycentricCoordinate
  public Tensor origin(Tensor levers) {
    return StaticHelper.barycentric(levers, genesis.origin(levers));
  }
}

// code by jph
package ch.ethz.idsc.sophus.gbc;

import java.io.Serializable;
import java.util.Objects;

import ch.ethz.idsc.sophus.krg.MetricDistances;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.ConstantArray;
import ch.ethz.idsc.tensor.api.ScalarUnaryOperator;

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
 * by Jan Hakenberg, 2020
 * 
 * @see MetricDistances */
public class MetricCoordinate implements Genesis, Serializable {
  private static final long serialVersionUID = -8043520781023560311L;

  /** Careful:
   * Distance may depend on sequence! In that case only the correct sequence
   * should be passed to the function {@link #weights(Tensor, Tensor)}!
   * 
   * @param genesis operator with design matrix as input
   * @return */
  public static Genesis of(Genesis genesis) {
    return new MetricCoordinate(Objects.requireNonNull(genesis));
  }

  /** @param variogram for example InversePowerVariogram.of(2)
   * @return */
  public static Genesis of(ScalarUnaryOperator variogram) {
    return of(new MetricWeighting(variogram));
  }

  /***************************************************/
  private static enum OneVector implements Genesis {
    INSTANCE;

    @Override // from Genesis
    public Tensor origin(Tensor levers) {
      return ConstantArray.of(RealScalar.ONE, levers.length());
    }
  }

  private static final Genesis AFFINE = new MetricCoordinate(OneVector.INSTANCE);

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

  /***************************************************/
  private final Genesis genesis;

  private MetricCoordinate(Genesis genesis) {
    this.genesis = genesis;
  }

  @Override // from BarycentricCoordinate
  public Tensor origin(Tensor levers) {
    return StaticHelper.barycentric( //
        genesis.origin(levers), //
        levers);
  }
}

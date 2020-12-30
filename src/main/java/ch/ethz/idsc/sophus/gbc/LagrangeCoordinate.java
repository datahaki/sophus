// code by jph
package ch.ethz.idsc.sophus.gbc;

import java.io.Serializable;
import java.util.Objects;

import ch.ethz.idsc.sophus.math.AffineQ;
import ch.ethz.idsc.sophus.math.NormalizeTotal;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.ConstantArray;
import ch.ethz.idsc.tensor.alg.Transpose;
import ch.ethz.idsc.tensor.alg.UnitVector;
import ch.ethz.idsc.tensor.ext.Cache;
import ch.ethz.idsc.tensor.mat.IdentityMatrix;
import ch.ethz.idsc.tensor.opt.rn.LagrangeMultiplier;
import ch.ethz.idsc.tensor.sca.Chop;

/** attempts to produce positive weights for levers with zero in convex hull
 * 
 * Technique of using Lagrange multipliers inspired by the following reference:
 * "Polygon Laplacian Made Simple"
 * by Astrid Bunge, Philipp Herholz, Misha Kazhdan, Mario Botsch, 2020 */
public class LagrangeCoordinate implements Genesis, Serializable {
  private static final Cache<Integer, Tensor> CACHE = Cache.of(IdentityMatrix::of, 10);

  /** @param genesis for instance InverseDistanceWeighting.of(InversePowerVariogram.of(2))
   * @return */
  public static Genesis of(Genesis genesis) {
    return new LagrangeCoordinate(Objects.requireNonNull(genesis));
  }

  /***************************************************/
  private final Genesis genesis;

  private LagrangeCoordinate(Genesis genesis) {
    this.genesis = genesis;
  }

  @Override // from Genesis
  public Tensor origin(Tensor levers) {
    return linearSolve(genesis.origin(levers), levers);
  }

  /** @param target
   * @param levers
   * @return */
  public static Tensor linearSolve(Tensor target, Tensor levers) {
    /* normalize total for improved numerics */
    return NormalizeTotal.FUNCTION.apply( //
        AffineQ.require(lagrangeMultiplier(target, levers).linearSolve(), Chop._06));
  }

  public static Tensor usingSvd(Tensor target, Tensor levers) {
    /* normalize total for improved numerics */
    return NormalizeTotal.FUNCTION.apply(lagrangeMultiplier(target, levers).usingSvd());
  }

  private static LagrangeMultiplier lagrangeMultiplier(Tensor target, Tensor levers) {
    int n = levers.length();
    Tensor eqs = Transpose.of(levers).append(ConstantArray.of(RealScalar.ONE, n));
    int d = eqs.length();
    Tensor rhs = UnitVector.of(d, d - 1);
    /* least squares is required if eqs do not have max rank, which is the case
     * when the tangent space parameterization is not 1 to 1 */
    return new LagrangeMultiplier(CACHE.apply(n), target, eqs, rhs);
  }
}

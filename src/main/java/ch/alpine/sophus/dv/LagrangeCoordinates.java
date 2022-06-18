// code by jph
package ch.alpine.sophus.dv;

import ch.alpine.sophus.hs.HsDesign;
import ch.alpine.sophus.math.AffineQ;
import ch.alpine.sophus.math.LagrangeMultiplier;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.ConstantArray;
import ch.alpine.tensor.alg.Transpose;
import ch.alpine.tensor.alg.UnitVector;
import ch.alpine.tensor.ext.Cache;
import ch.alpine.tensor.mat.IdentityMatrix;
import ch.alpine.tensor.nrm.NormalizeTotal;
import ch.alpine.tensor.sca.Chop;

/** @see HsDesign */
public enum LagrangeCoordinates {
  ;
  private static final int CACHE_SIZE = 10;
  private static final Cache<Integer, Tensor> CACHE = Cache.of(IdentityMatrix::of, CACHE_SIZE);

  /** @param levers
   * @param target
   * @return */
  public static Tensor of(Tensor levers, Tensor target) {
    int n = levers.length();
    Tensor eqs = Transpose.of(levers).append(ConstantArray.of(RealScalar.ONE, n));
    int d = eqs.length();
    Tensor rhs = UnitVector.of(d, d - 1);
    /* least squares is required if eqs do not have max rank, which is the case
     * when the tangent space parameterization is not 1 to 1 */
    Tensor weights = new LagrangeMultiplier(CACHE.apply(n), target, eqs, rhs).solve();
    AffineQ.require(weights, Chop._02); // conceptual check
    return NormalizeTotal.FUNCTION.apply(weights);
  }
}

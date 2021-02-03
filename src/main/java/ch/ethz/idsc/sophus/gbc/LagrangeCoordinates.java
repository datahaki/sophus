// code by jph
package ch.ethz.idsc.sophus.gbc;

import ch.ethz.idsc.sophus.math.NormalizeTotal;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.ConstantArray;
import ch.ethz.idsc.tensor.alg.Transpose;
import ch.ethz.idsc.tensor.alg.UnitVector;
import ch.ethz.idsc.tensor.ext.Cache;
import ch.ethz.idsc.tensor.mat.IdentityMatrix;
import ch.ethz.idsc.tensor.opt.rn.LagrangeMultiplier;

public enum LagrangeCoordinates {
  ;
  private static final Cache<Integer, Tensor> CACHE = Cache.of(IdentityMatrix::of, 10);

  /** @param target
   * @param levers
   * @return */
  public static Tensor of(Tensor target, Tensor levers) {
    int n = levers.length();
    Tensor eqs = Transpose.of(levers).append(ConstantArray.of(RealScalar.ONE, n));
    int d = eqs.length();
    Tensor rhs = UnitVector.of(d, d - 1);
    /* least squares is required if eqs do not have max rank, which is the case
     * when the tangent space parameterization is not 1 to 1 */
    Tensor solve = new LagrangeMultiplier(CACHE.apply(n), target, eqs, rhs).solve();
    return NormalizeTotal.FUNCTION.apply(solve);
  }
}

// code by jph
package ch.ethz.idsc.sophus.gbc;

import ch.ethz.idsc.sophus.hs.HsDesign;
import ch.ethz.idsc.sophus.math.AffineQ;
import ch.ethz.idsc.sophus.math.LagrangeMultiplier;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.ConstantArray;
import ch.ethz.idsc.tensor.alg.Transpose;
import ch.ethz.idsc.tensor.alg.UnitVector;
import ch.ethz.idsc.tensor.ext.Cache;
import ch.ethz.idsc.tensor.mat.IdentityMatrix;
import ch.ethz.idsc.tensor.nrm.NormalizeTotal;
import ch.ethz.idsc.tensor.sca.Chop;

/** @see HsDesign */
public enum LagrangeCoordinates {
  ;
  private static final Cache<Integer, Tensor> CACHE = Cache.of(IdentityMatrix::of, 10);

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

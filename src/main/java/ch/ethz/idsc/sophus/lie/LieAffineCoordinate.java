// code by jph
package ch.ethz.idsc.sophus.lie;

import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.alg.ConstantArray;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;

/** Lie affine coordinates are generalized barycentric coordinates for
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
 * Ad[g].Log[g^-1.m] == Log[m.g^-1] */
public class LieAffineCoordinate extends LieBarycentricCoordinate {
  /** @param lieGroup
   * @param log
   * @throws Exception if any input parameter is null */
  public LieAffineCoordinate(LieGroup lieGroup, TensorUnaryOperator log) {
    super(lieGroup, log, levers -> ConstantArray.of(RealScalar.ONE, levers.length()));
  }
}

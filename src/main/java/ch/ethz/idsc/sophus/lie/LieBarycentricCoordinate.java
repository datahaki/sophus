// code by jph
package ch.ethz.idsc.sophus.lie;

import ch.ethz.idsc.sophus.hs.HsBarycentricCoordinate;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;

/** invariance under left-action is guaranteed because
 * log [(g x)^-1 g p] == log [x^-1 p]
 * 
 * If the target mapping is Ad invariant then invariance under right action
 * and inversion is guaranteed.
 * 
 * If the target mapping correlates to inverse distances then the coordinates
 * satisfy the Lagrange property.
 * 
 * @see LieBiinvariantCoordinate */
public class LieBarycentricCoordinate extends HsBarycentricCoordinate {
  /** @param lieGroup
   * @param log mapping from group to Lie algebra
   * @param target
   * @throws Exception if any input parameter is null */
  public LieBarycentricCoordinate(LieGroup lieGroup, TensorUnaryOperator log, TensorUnaryOperator target) {
    super(LieFlattenLogManifold.of(lieGroup, log), target);
  }
}

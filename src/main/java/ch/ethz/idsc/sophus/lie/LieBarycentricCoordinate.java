// code by jph
package ch.ethz.idsc.sophus.lie;

import java.util.Objects;

import ch.ethz.idsc.sophus.hs.HsBarycentricCoordinate;
import ch.ethz.idsc.tensor.Tensor;
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
  private final LieGroup lieGroup;
  private final TensorUnaryOperator log;

  /** @param lieGroup
   * @param log mapping from group to Lie algebra
   * @param target
   * @throws Exception if any input parameter is null */
  public LieBarycentricCoordinate(LieGroup lieGroup, TensorUnaryOperator log, TensorUnaryOperator target) {
    super(target);
    this.lieGroup = Objects.requireNonNull(lieGroup);
    this.log = Objects.requireNonNull(log);
  }

  @Override // from HsBiinvariantCoordinate
  public final FlattenLog logAt(Tensor point) {
    LieGroupElement lieGroupElement = lieGroup.element(point).inverse();
    return q -> log.apply(lieGroupElement.combine(q));
  }
}

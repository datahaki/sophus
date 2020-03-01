// code by jph
package ch.ethz.idsc.sophus.lie;

import java.util.Objects;

import ch.ethz.idsc.sophus.hs.HsBiinvariantCoordinate;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;

/** invariant under left-action, right-action, and inversion
 * 
 * Reference:
 * "Biinvariant Coordinates in Lie Groups"
 * by Jan Hakenberg, 2020 */
public class LieBiinvariantCoordinate extends HsBiinvariantCoordinate {
  private final LieGroup lieGroup;
  private final TensorUnaryOperator log;

  /** @param lieGroup
   * @param log mapping from group to Lie algebra
   * @param target
   * @throws Exception if any input parameter is null */
  public LieBiinvariantCoordinate(LieGroup lieGroup, TensorUnaryOperator log, TensorUnaryOperator target) {
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

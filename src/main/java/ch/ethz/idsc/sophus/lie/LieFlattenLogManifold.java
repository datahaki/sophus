// code by jph
package ch.ethz.idsc.sophus.lie;

import java.io.Serializable;
import java.util.Objects;

import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;

public class LieFlattenLogManifold implements FlattenLogManifold, Serializable {
  /** @param lieGroup
   * @param log
   * @return */
  public static FlattenLogManifold of(LieGroup lieGroup, TensorUnaryOperator log) {
    return new LieFlattenLogManifold(lieGroup, log);
  }

  /***************************************************/
  private final LieGroup lieGroup;
  private final TensorUnaryOperator log;

  private LieFlattenLogManifold(LieGroup lieGroup, TensorUnaryOperator log) {
    this.lieGroup = Objects.requireNonNull(lieGroup);
    this.log = Objects.requireNonNull(log);
  }

  @Override // from HsBiinvariantCoordinate
  public final FlattenLog logAt(Tensor point) {
    LieGroupElement lieGroupElement = lieGroup.element(point).inverse();
    return q -> log.apply(lieGroupElement.combine(q));
  }
}

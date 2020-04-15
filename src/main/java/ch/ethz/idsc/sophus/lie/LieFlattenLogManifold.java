// code by jph
package ch.ethz.idsc.sophus.lie;

import java.io.Serializable;
import java.util.Objects;

import ch.ethz.idsc.sophus.hs.FlattenLog;
import ch.ethz.idsc.sophus.hs.FlattenLogManifold;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;

public class LieFlattenLogManifold implements FlattenLogManifold, Serializable {
  /** @param lieGroup
   * @param log
   * @return */
  public static FlattenLogManifold of(LieGroup lieGroup, TensorUnaryOperator log) {
    return new LieFlattenLogManifold(Objects.requireNonNull(lieGroup), Objects.requireNonNull(log));
  }

  /***************************************************/
  private final LieGroup lieGroup;
  private final TensorUnaryOperator log;

  private LieFlattenLogManifold(LieGroup lieGroup, TensorUnaryOperator log) {
    this.lieGroup = lieGroup;
    this.log = log;
  }

  @Override // from FlattenLogManifold
  public FlattenLog logAt(Tensor point) {
    return new FlattenLogImpl(point);
  }

  /***************************************************/
  private class FlattenLogImpl implements FlattenLog, Serializable {
    private final LieGroupElement lieGroupElement;

    public FlattenLogImpl(Tensor point) {
      this.lieGroupElement = lieGroup.element(point).inverse();
    }

    @Override
    public Tensor flattenLog(Tensor q) {
      return log.apply(lieGroupElement.combine(q));
    }
  }
}

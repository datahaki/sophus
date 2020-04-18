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
   * @param flattenLog
   * @return */
  public static FlattenLogManifold of(LieGroup lieGroup, TensorUnaryOperator flattenLog) {
    return new LieFlattenLogManifold( //
        Objects.requireNonNull(lieGroup), //
        Objects.requireNonNull(flattenLog));
  }

  /***************************************************/
  private final LieGroup lieGroup;
  private final TensorUnaryOperator flattenLog;

  private LieFlattenLogManifold(LieGroup lieGroup, TensorUnaryOperator flattenLog) {
    this.lieGroup = lieGroup;
    this.flattenLog = flattenLog;
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
      return flattenLog.apply(lieGroupElement.combine(q));
    }
  }
}

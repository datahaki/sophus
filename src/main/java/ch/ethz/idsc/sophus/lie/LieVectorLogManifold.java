// code by jph
package ch.ethz.idsc.sophus.lie;

import java.io.Serializable;
import java.util.Objects;

import ch.ethz.idsc.sophus.hs.TangentSpace;
import ch.ethz.idsc.sophus.hs.VectorLogManifold;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;

public class LieVectorLogManifold implements VectorLogManifold, Serializable {
  /** @param lieGroup
   * @param flattenLog
   * @return */
  public static VectorLogManifold of(LieGroup lieGroup, TensorUnaryOperator flattenLog) {
    return new LieVectorLogManifold( //
        Objects.requireNonNull(lieGroup), //
        Objects.requireNonNull(flattenLog));
  }

  /***************************************************/
  private final LieGroup lieGroup;
  private final TensorUnaryOperator flattenLog;

  private LieVectorLogManifold(LieGroup lieGroup, TensorUnaryOperator flattenLog) {
    this.lieGroup = lieGroup;
    this.flattenLog = flattenLog;
  }

  @Override // from FlattenLogManifold
  public TangentSpace logAt(Tensor point) {
    return new FlattenLogImpl(point);
  }

  /***************************************************/
  private class FlattenLogImpl implements TangentSpace, Serializable {
    private final LieGroupElement lieGroupElement;

    public FlattenLogImpl(Tensor point) {
      this.lieGroupElement = lieGroup.element(point).inverse();
    }

    @Override
    public Tensor vectorLog(Tensor q) {
      return flattenLog.apply(lieGroupElement.combine(q));
    }
  }
}

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
   * @param vectorLog
   * @return */
  public static VectorLogManifold of(LieGroup lieGroup, TensorUnaryOperator vectorLog) {
    return new LieVectorLogManifold( //
        Objects.requireNonNull(lieGroup), //
        Objects.requireNonNull(vectorLog));
  }

  /***************************************************/
  private final LieGroup lieGroup;
  private final TensorUnaryOperator vectorLog;

  private LieVectorLogManifold(LieGroup lieGroup, TensorUnaryOperator vectorLog) {
    this.lieGroup = lieGroup;
    this.vectorLog = vectorLog;
  }

  @Override // from VectorLogManifold
  public TangentSpace logAt(Tensor point) {
    return new TangentSpaceImpl(point);
  }

  /***************************************************/
  private class TangentSpaceImpl implements TangentSpace, Serializable {
    private final LieGroupElement lieGroupElement;

    public TangentSpaceImpl(Tensor point) {
      this.lieGroupElement = lieGroup.element(point).inverse();
    }

    @Override
    public Tensor vectorLog(Tensor q) {
      return vectorLog.apply(lieGroupElement.combine(q));
    }
  }
}

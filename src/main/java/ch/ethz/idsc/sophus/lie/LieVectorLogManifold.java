// code by jph
package ch.ethz.idsc.sophus.lie;

import java.io.Serializable;
import java.util.Objects;

import ch.ethz.idsc.sophus.hs.TangentSpace;
import ch.ethz.idsc.sophus.hs.VectorLogManifold;
import ch.ethz.idsc.tensor.Tensor;

public class LieVectorLogManifold implements VectorLogManifold, Serializable {
  /** @param lieGroup
   * @param tangentSpace
   * @return */
  public static VectorLogManifold of(LieGroup lieGroup, TangentSpace tangentSpace) {
    return new LieVectorLogManifold( //
        Objects.requireNonNull(lieGroup), //
        Objects.requireNonNull(tangentSpace));
  }

  /***************************************************/
  private final LieGroup lieGroup;
  private final TangentSpace tangentSpace;

  private LieVectorLogManifold(LieGroup lieGroup, TangentSpace tangentSpace) {
    this.lieGroup = lieGroup;
    this.tangentSpace = tangentSpace;
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

    @Override // from TangentSpace
    public Tensor vectorLog(Tensor q) {
      return tangentSpace.vectorLog(lieGroupElement.combine(q));
    }
  }
}

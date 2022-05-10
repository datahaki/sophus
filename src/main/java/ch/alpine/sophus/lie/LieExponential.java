// code by jph
package ch.alpine.sophus.lie;

import java.io.Serializable;
import java.util.Objects;

import ch.alpine.sophus.api.Exponential;
import ch.alpine.sophus.hs.HsManifold;
import ch.alpine.sophus.hs.HsTransport;
import ch.alpine.sophus.hs.TangentSpace;
import ch.alpine.tensor.Tensor;

/** all tangent vectors are assumed to be in the tangent space at the neutral element,
 * i.e. given in the basis of TeG */
public final class LieExponential implements HsManifold, Serializable {
  /** @param lieGroup G
   * @return */
  public static LieExponential of(LieGroup lieGroup) {
    return new LieExponential(Objects.requireNonNull(lieGroup));
  }

  // ---
  private final LieGroup lieGroup;
  private final Exponential exponential;

  private LieExponential(LieGroup lieGroup) {
    this.lieGroup = lieGroup;
    this.exponential = lieGroup.exponential();
  }

  @Override // from VectorLogManifold
  public TangentSpace logAt(Tensor point) {
    return new ExponentialImpl(point);
  }

  @Override // from HsManifold
  public Exponential exponential(Tensor point) {
    return new ExponentialImpl(point);
  }

  private class ExponentialImpl implements Exponential, Serializable {
    private final LieGroupElement element;
    private final LieGroupElement inverse;

    public ExponentialImpl(Tensor p) {
      element = lieGroup.element(p);
      inverse = element.inverse();
    }

    @Override // from Exponential
    public Tensor exp(Tensor v) {
      return element.combine(exponential.exp(v));
    }

    @Override // from Exponential
    public Tensor log(Tensor q) {
      return exponential.log(inverse.combine(q));
    }

    @Override // from TangentSpace
    public Tensor vectorLog(Tensor q) {
      return exponential.vectorLog(inverse.combine(q));
    }
  }

  @Override
  public HsTransport hsTransport() {
    return LieTransport.INSTANCE;
  }
}

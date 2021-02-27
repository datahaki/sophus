// code by jph
package ch.ethz.idsc.sophus.lie;

import java.io.Serializable;
import java.util.Objects;

import ch.ethz.idsc.sophus.hs.HsManifold;
import ch.ethz.idsc.sophus.hs.TangentSpace;
import ch.ethz.idsc.sophus.math.Exponential;
import ch.ethz.idsc.tensor.Tensor;

/** all tangent vectors are assumed to be in the tangent space at the neutral element,
 * i.e. given in the basis of TeG */
public class LieExponential implements HsManifold, Serializable {
  /** @param lieGroup G
   * @param exponential at TeG
   * @return */
  public static LieExponential of(LieGroup lieGroup, Exponential exponential) {
    return new LieExponential( //
        Objects.requireNonNull(lieGroup), //
        Objects.requireNonNull(exponential));
  }

  /***************************************************/
  private final LieGroup lieGroup;
  private final Exponential exponential;

  private LieExponential(LieGroup lieGroup, Exponential exponential) {
    this.lieGroup = lieGroup;
    this.exponential = exponential;
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
}

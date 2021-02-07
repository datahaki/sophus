// code by jph
package ch.ethz.idsc.sophus.lie;

import java.io.Serializable;
import java.util.Objects;

import ch.ethz.idsc.sophus.hs.HsExponential;
import ch.ethz.idsc.sophus.hs.TangentSpace;
import ch.ethz.idsc.sophus.math.Exponential;
import ch.ethz.idsc.tensor.Tensor;

/** all tangent vectors are assumed to be in the tangent space at the neutral element,
 * i.e. given in the basis of TeG */
public class LieExponential implements HsExponential, Serializable {
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

  @Override // from HsExponential
  public Exponential exponential(Tensor point) {
    return new ExponentialImpl(point);
  }

  @Override // from VectorLogManifold
  public TangentSpace logAt(Tensor point) {
    return new ExponentialImpl(point);
  }

  private class ExponentialImpl implements Exponential, Serializable {
    private final LieGroupElement element;
    private final LieGroupElement inverse;

    public ExponentialImpl(Tensor point) {
      element = lieGroup.element(point);
      inverse = element.inverse();
    }

    @Override // from Exponential
    public Tensor exp(Tensor vector) {
      return element.combine(exponential.exp(vector));
    }

    @Override // from Exponential
    public Tensor log(Tensor point) {
      return exponential.log(inverse.combine(point));
    }

    @Override // from TangentSpace
    public Tensor vectorLog(Tensor point) {
      return exponential.vectorLog(inverse.combine(point));
    }
  }
}

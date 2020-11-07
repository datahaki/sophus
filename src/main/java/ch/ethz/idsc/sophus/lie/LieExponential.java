// code by jph
package ch.ethz.idsc.sophus.lie;

import java.io.Serializable;
import java.util.Objects;

import ch.ethz.idsc.sophus.hs.HsExponential;
import ch.ethz.idsc.sophus.math.Exponential;
import ch.ethz.idsc.tensor.Tensor;

/**
 * 
 */
public class LieExponential implements HsExponential, Serializable {
  private static final long serialVersionUID = -1771924999128144298L;

  /** @param lieGroup
   * @param exponential
   * @return */
  public static HsExponential of(LieGroup lieGroup, Exponential exponential) {
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

  private class ExponentialImpl implements Exponential, Serializable {
    private static final long serialVersionUID = 1572938118717771657L;
    // ---
    private final LieGroupElement element;
    private final LieGroupElement inverse;

    public ExponentialImpl(Tensor point) {
      element = lieGroup.element(point);
      inverse = element.inverse();
    }

    @Override // from Exponential
    public Tensor exp(Tensor vector) {
      // TODO x is tangent vector at point (not at neutral elem.)
      return element.combine(exponential.exp(vector));
    }

    @Override // from Exponential
    public Tensor log(Tensor point) {
      return exponential.log(inverse.combine(point));
    }
  }
}

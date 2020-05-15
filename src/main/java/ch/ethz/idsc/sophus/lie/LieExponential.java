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
  /** @param lieGroup
   * @param exponential
   * @return */
  public static HsExponential of(LieGroup lieGroup, Exponential exponential) {
    return new LieExponential(lieGroup, exponential);
  }

  /***************************************************/
  private final LieGroup lieGroup;
  private final Exponential exponential;

  private LieExponential(LieGroup lieGroup, Exponential exponential) {
    this.lieGroup = Objects.requireNonNull(lieGroup);
    this.exponential = Objects.requireNonNull(exponential);
  }

  @Override // from HsExponential
  public Exponential exponential(Tensor point) {
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
    public Tensor exp(Tensor x) { // TODO x is tangent vector at point (not at neutral elem.)
      return element.combine(exponential.exp(x));
    }

    @Override // from Exponential
    public Tensor log(Tensor g) {
      return exponential.log(inverse.combine(g));
    }
  }
}

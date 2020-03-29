// code by jph
package ch.ethz.idsc.sophus.lie;

import java.io.Serializable;
import java.util.Objects;

import ch.ethz.idsc.sophus.hs.HsExponential;
import ch.ethz.idsc.sophus.math.Exponential;
import ch.ethz.idsc.tensor.Tensor;

public class PointLieExponential implements HsExponential, Serializable {
  /** @param lieGroup
   * @param lieExponential
   * @return */
  public static HsExponential of(LieGroup lieGroup, Exponential lieExponential) {
    return new PointLieExponential(lieGroup, lieExponential);
  }

  /***************************************************/
  private final LieGroup lieGroup;
  private final Exponential lieExponential;

  private PointLieExponential(LieGroup lieGroup, Exponential lieExponential) {
    this.lieGroup = Objects.requireNonNull(lieGroup);
    this.lieExponential = Objects.requireNonNull(lieExponential);
  }

  @Override
  public Exponential exponential(Tensor point) {
    return new PointExp(point);
  }

  public class PointExp implements Exponential, Serializable {
    private final LieGroupElement element;
    private final LieGroupElement inverse;

    public PointExp(Tensor point) {
      element = lieGroup.element(point);
      inverse = element.inverse();
    }

    @Override
    public Tensor exp(Tensor x) {
      return element.combine(lieExponential.exp(x));
    }

    @Override
    public Tensor log(Tensor g) {
      return lieExponential.log(inverse.combine(g));
    }
  }
}

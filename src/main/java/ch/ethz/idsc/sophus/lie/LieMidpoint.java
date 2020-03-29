// code by jph
package ch.ethz.idsc.sophus.lie;

import java.util.Objects;

import ch.ethz.idsc.sophus.math.Exponential;
import ch.ethz.idsc.sophus.math.MidpointInterface;
import ch.ethz.idsc.tensor.RationalScalar;
import ch.ethz.idsc.tensor.Tensor;

// TODO can use HsMidpoint + EXP
public class LieMidpoint implements MidpointInterface {
  private final LieGroup lieGroup;
  private final Exponential lieExponential;

  /** @param lieGroup non-null
   * @param lieExponential non-null */
  public LieMidpoint(LieGroup lieGroup, Exponential lieExponential) {
    this.lieGroup = Objects.requireNonNull(lieGroup);
    this.lieExponential = Objects.requireNonNull(lieExponential);
  }

  @Override // from MidpointInterface
  public Tensor midpoint(Tensor p, Tensor q) {
    LieGroupElement lieGroupElement = lieGroup.element(p);
    LieGroupElement inverse = lieGroupElement.inverse();
    Tensor log = lieExponential.log(inverse.combine(q));
    return lieGroupElement.combine(lieExponential.exp(log.multiply(RationalScalar.HALF)));
  }
}

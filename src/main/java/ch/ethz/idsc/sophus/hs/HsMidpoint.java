// code by jph
package ch.ethz.idsc.sophus.hs;

import java.io.Serializable;
import java.util.Objects;

import ch.ethz.idsc.sophus.math.Exponential;
import ch.ethz.idsc.sophus.math.MidpointInterface;
import ch.ethz.idsc.tensor.RationalScalar;
import ch.ethz.idsc.tensor.Tensor;

public class HsMidpoint implements MidpointInterface, Serializable {
  private static final long serialVersionUID = -7787681650782693896L;

  /** @param exponential
   * @param q
   * @return */
  public static Tensor of(Exponential exponential, Tensor q) {
    return exponential.exp(exponential.log(q).multiply(RationalScalar.HALF));
  }

  /***************************************************/
  private final HsExponential hsExponential;

  /** @param hsExponential */
  public HsMidpoint(HsExponential hsExponential) {
    this.hsExponential = Objects.requireNonNull(hsExponential);
  }

  @Override // from MidpointInterface
  public Tensor midpoint(Tensor p, Tensor q) {
    return of(hsExponential.exponential(p), q);
  }
}

// code by jph
package ch.ethz.idsc.sophus.hs;

import java.io.Serializable;
import java.util.Objects;

import ch.ethz.idsc.sophus.math.Exponential;
import ch.ethz.idsc.sophus.math.MidpointInterface;
import ch.ethz.idsc.tensor.RationalScalar;
import ch.ethz.idsc.tensor.Tensor;

public class HsMidpoint implements MidpointInterface, Serializable {
  private final HsExponential hsExponential;

  /** @param hsExponential */
  public HsMidpoint(HsExponential hsExponential) {
    this.hsExponential = Objects.requireNonNull(hsExponential);
  }

  @Override // from MidpointInterface
  public Tensor midpoint(Tensor p, Tensor q) {
    Exponential exponential = hsExponential.exponential(p);
    return exponential.exp(exponential.log(q).multiply(RationalScalar.HALF));
  }
}

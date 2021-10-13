// code by jph
package ch.alpine.sophus.hs;

import java.io.Serializable;
import java.util.Objects;

import ch.alpine.sophus.math.Exponential;
import ch.alpine.sophus.math.MidpointInterface;
import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.Tensor;

public class HsMidpoint implements MidpointInterface, Serializable {
  /** @param exponential
   * @param q
   * @return */
  public static Tensor of(Exponential exponential, Tensor q) {
    return exponential.exp(exponential.log(q).multiply(RationalScalar.HALF));
  }

  // ---
  private final HsManifold hsManifold;

  /** @param hsManifold */
  public HsMidpoint(HsManifold hsManifold) {
    this.hsManifold = Objects.requireNonNull(hsManifold);
  }

  @Override // from MidpointInterface
  public Tensor midpoint(Tensor p, Tensor q) {
    return of(hsManifold.exponential(p), q);
  }
}

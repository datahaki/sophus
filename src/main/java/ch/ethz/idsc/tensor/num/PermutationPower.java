// code by jph
package ch.ethz.idsc.tensor.num;

import ch.ethz.idsc.tensor.alg.BinaryPower;

/** Hint: implementation is not most efficient! */
public class PermutationPower extends BinaryPower<Cycles> {
  /** @param cycles
   * @param exponent
   * @return */
  public static Cycles of(Cycles cycles, long exponent) {
    return new PermutationPower().apply(cycles, exponent);
  }

  /***************************************************/
  private PermutationPower() {
    // ---
  }

  @Override // from BinaryPower
  protected Cycles zeroth() {
    return Cycles.identity();
  }

  @Override // from BinaryPower
  protected Cycles invert(Cycles cycles) {
    return cycles.inverse();
  }

  @Override // from BinaryPower
  protected Cycles multiply(Cycles cycles1, Cycles cycles2) {
    return cycles1.product(cycles2);
  }
}

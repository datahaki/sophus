// code by jph
package ch.ethz.idsc.tensor.num;

/** inspired by
 * <a href="https://reference.wolfram.com/language/ref/InversePermutation.html">InversePermutation</a> */
public enum InversePermutation {
  ;
  /** @param cycles
   * @return */
  public static Cycles of(Cycles cycles) {
    return cycles.inverse();
  }
}

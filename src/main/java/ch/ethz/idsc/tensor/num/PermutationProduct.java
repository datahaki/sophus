// code by jph
package ch.ethz.idsc.tensor.num;

/** inspired by
 * <a href="https://reference.wolfram.com/language/ref/PermutationProduct.html">PermutationProduct</a> */
public enum PermutationProduct {
  ;
  /** @param cycles1
   * @param cycles2
   * @return */
  public static Cycles of(Cycles cycles1, Cycles cycles2) {
    return cycles1.product(cycles2);
  }
}

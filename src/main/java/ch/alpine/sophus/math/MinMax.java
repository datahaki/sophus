// code by jph
package ch.alpine.sophus.math;

import java.io.Serializable;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.red.Entrywise;

/** inspired by
 * <a href="https://reference.wolfram.com/language/ref/MinMax.html">MinMax</a> */
public class MinMax implements Serializable {
  /** @param tensor not a scalar
   * @return */
  public static MinMax of(Tensor tensor) {
    return new MinMax(tensor);
  }

  /***************************************************/
  private final Tensor min;
  private final Tensor max;

  private MinMax(Tensor tensor) {
    min = Entrywise.min().of(tensor);
    max = Entrywise.max().of(tensor);
  }

  public Tensor min() {
    return min.unmodifiable();
  }

  public Tensor max() {
    return max.unmodifiable();
  }
}

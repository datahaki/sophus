// code by jph
package ch.alpine.sophus.math;

import java.io.Serializable;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.opt.nd.NdBox;
import ch.alpine.tensor.red.Entrywise;

/** inspired by
 * <a href="https://reference.wolfram.com/language/ref/MinMax.html">MinMax</a> */
public class MinMax implements Serializable {
  /** @param tensor not a scalar
   * @return */
  public static MinMax of(Tensor tensor) {
    return new MinMax(tensor);
  }

  public static NdBox ndBox(Tensor tensor) {
    return new MinMax(tensor).ndBox();
  }

  // ==================================================
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

  public NdBox ndBox() {
    return NdBox.of(min, max);
  }

  /** @return 2 x N matrix */
  public Tensor matrix() {
    return Tensors.of(min, max);
  }
}

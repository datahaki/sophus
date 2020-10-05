// code by jph
package ch.ethz.idsc.sophus.flt;

import java.util.Objects;
import java.util.function.Supplier;

import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;

public class CausalFilter implements TensorUnaryOperator {
  private static final long serialVersionUID = -5214885971431533037L;

  /** @param supplier
   * @return */
  public static TensorUnaryOperator of(Supplier<TensorUnaryOperator> supplier) {
    return new CausalFilter(Objects.requireNonNull(supplier));
  }

  /***************************************************/
  private final Supplier<TensorUnaryOperator> supplier;

  private CausalFilter(Supplier<TensorUnaryOperator> supplier) {
    this.supplier = supplier;
  }

  @Override
  public Tensor apply(Tensor tensor) {
    return Tensor.of(tensor.stream().map(supplier.get()));
  }
}

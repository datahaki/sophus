// code by jph
package ch.alpine.sophus.flt;

import java.util.Objects;
import java.util.function.Supplier;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.TensorUnaryOperator;

public class CausalFilter implements TensorUnaryOperator {
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

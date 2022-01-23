// code by jph
package ch.alpine.sophus.hs;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.BinaryOperator;

import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.itp.BinaryAverage;

public class HsBinaryAverage implements BinaryAverage, Serializable {
  /** @param bch non-null */
  public static BinaryAverage of(BinaryOperator<Tensor> bch) {
    return new HsBinaryAverage(Objects.requireNonNull(bch));
  }

  // ---
  private final BinaryOperator<Tensor> bch;

  private HsBinaryAverage(BinaryOperator<Tensor> bch) {
    this.bch = bch;
  }

  @Override // from BinaryAverage
  public Tensor split(Tensor p, Tensor q, Scalar scalar) {
    return bch.apply(p, bch.apply(p.negate(), q).multiply(scalar));
  }
}

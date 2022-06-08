// code by jph
package ch.alpine.sophus.lie;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.BinaryOperator;

import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.itp.BinaryAverage;

// TODO SOPHUS implement as GeoSpace!
public class BchBinaryAverage implements BinaryAverage, Serializable {
  /** @param bch non-null */
  public static BinaryAverage of(BinaryOperator<Tensor> bch) {
    return new BchBinaryAverage(Objects.requireNonNull(bch));
  }

  // ---
  private final BinaryOperator<Tensor> bch;

  private BchBinaryAverage(BinaryOperator<Tensor> bch) {
    this.bch = bch;
  }

  @Override // from BinaryAverage
  public Tensor split(Tensor p, Tensor q, Scalar scalar) {
    return bch.apply(p, bch.apply(p.negate(), q).multiply(scalar));
  }
}

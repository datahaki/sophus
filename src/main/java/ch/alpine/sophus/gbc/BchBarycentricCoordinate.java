// code by jph
package ch.alpine.sophus.gbc;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.BinaryOperator;

import ch.alpine.sophus.math.Genesis;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.ScalarUnaryOperator;

public class BchBarycentricCoordinate implements BarycentricCoordinate, Serializable {
  /** @param bch non-null
   * @param variogram for instance InversePowerVariogram.of(2)
   * @return */
  public static BarycentricCoordinate of(BinaryOperator<Tensor> bch, ScalarUnaryOperator variogram) {
    return new BchBarycentricCoordinate( //
        Objects.requireNonNull(bch), //
        Objects.requireNonNull(variogram));
  }

  // ---
  private final BinaryOperator<Tensor> bch;
  private final Genesis genesis;

  private BchBarycentricCoordinate(BinaryOperator<Tensor> bch, ScalarUnaryOperator variogram) {
    this.bch = bch;
    genesis = LeveragesGenesis.of(variogram);
  }

  @Override // from BarycentricCoordinate
  public Tensor weights(Tensor sequence, Tensor point) {
    Tensor p = point.negate();
    return genesis.origin(Tensor.of(sequence.stream().map(q -> bch.apply(p, q))));
  }
}

// code by jph
package ch.alpine.sophus.dv;

import java.io.Serializable;
import java.util.function.BinaryOperator;

import ch.alpine.sophus.hs.Genesis;
import ch.alpine.sophus.hs.ad.HsBarycentricCoordinate;
import ch.alpine.tensor.Tensor;

/** @see HsBarycentricCoordinate
 * 
 * @param bch non-null
 * @param genesis for instance InversePowerVariogram.of(2) */
public record BchBarycentricCoordinate(BinaryOperator<Tensor> bch, Genesis genesis) //
    implements BarycentricCoordinate, Serializable {
  @Override // from BarycentricCoordinate
  public Tensor weights(Tensor sequence, Tensor point) {
    Tensor p_inv = point.negate();
    return genesis.origin(Tensor.of(sequence.stream().map(q -> bch.apply(p_inv, q))));
  }
}

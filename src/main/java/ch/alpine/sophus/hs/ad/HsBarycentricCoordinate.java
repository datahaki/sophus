// code by jph
package ch.alpine.sophus.hs.ad;

import java.io.Serializable;

import ch.alpine.sophus.api.Genesis;
import ch.alpine.sophus.gbc.BarycentricCoordinate;
import ch.alpine.sophus.gbc.BchBarycentricCoordinate;
import ch.alpine.tensor.Tensor;

/** @see BchBarycentricCoordinate
 * 
 * @param hsAlgebra
 * @param genesis for instance new LeveragesGenesis(InversePowerVariogram.of(2)), i.e.
 * LeveragesGenesis.DEFAULT */
public record HsBarycentricCoordinate(HsAlgebra hsAlgebra, Genesis genesis) //
    implements BarycentricCoordinate, Serializable {
  @Override // from BarycentricCoordinate
  public Tensor weights(Tensor sequence, Tensor point) {
    Tensor p_inv = hsAlgebra.lift(point).negate();
    return genesis.origin(Tensor.of(sequence.stream().map(hsAlgebra.action(p_inv))));
  }
}

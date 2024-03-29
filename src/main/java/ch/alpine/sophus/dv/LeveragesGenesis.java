// code by jph
package ch.alpine.sophus.dv;

import java.util.Objects;

import ch.alpine.sophus.hs.Genesis;
import ch.alpine.sophus.math.var.InversePowerVariogram;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.ScalarUnaryOperator;
import ch.alpine.tensor.mat.gr.InfluenceMatrix;
import ch.alpine.tensor.mat.gr.Mahalanobis;
import ch.alpine.tensor.nrm.NormalizeTotal;

/** target coordinate is the preferred way to evaluate
 * inverse leverage coordinates.
 * 
 * <p>References:
 * Reference:
 * "Biinvariant Generalized Barycentric Coordinates on Lie Groups"
 * by Jan Hakenberg, 2020
 * 
 * "Biinvariant Distance Vectors"
 * by Jan Hakenberg, 2020
 * 
 * @see LeveragesCoordinate
 * 
 * @param variogram for instance InversePowerVariogram */
public record LeveragesGenesis(ScalarUnaryOperator variogram) implements Genesis {
  public static final Genesis DEFAULT = new LeveragesGenesis(InversePowerVariogram.of(2));

  public LeveragesGenesis {
    Objects.requireNonNull(variogram);
  }

  @Override // from Genesis
  public Tensor origin(Tensor levers) {
    InfluenceMatrix influenceMatrix = new Mahalanobis(levers);
    Tensor vector = NormalizeTotal.FUNCTION.apply(influenceMatrix.leverages_sqrt().map(variogram));
    return StaticHelper.barycentric(influenceMatrix, vector);
  }
}

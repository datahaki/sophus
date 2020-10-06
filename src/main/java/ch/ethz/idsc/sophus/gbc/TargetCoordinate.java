// code by jph
package ch.ethz.idsc.sophus.gbc;

import ch.ethz.idsc.sophus.hs.Mahalanobis;
import ch.ethz.idsc.sophus.math.NormalizeTotal;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;
import ch.ethz.idsc.tensor.sca.ScalarUnaryOperator;

/** target coordinate is the preferred way to evaluate
 * inverse leverage coordinates.
 * 
 * the slower alternative is {@link AnchorCoordinate}.
 * 
 * <p>Reference:
 * "Biinvariant Distance Vectors"
 * by Jan Hakenberg, 2020
 * 
 * @see LeverageCoordinate */
/* package */ class TargetCoordinate implements TensorUnaryOperator {
  private static final long serialVersionUID = -8582272887789104693L;
  // ---
  private final ScalarUnaryOperator variogram;

  public TargetCoordinate(ScalarUnaryOperator variogram) {
    this.variogram = variogram;
  }

  @Override
  public Tensor apply(Tensor design) {
    Mahalanobis mahalanobis = new Mahalanobis(design);
    return StaticHelper.barycentric( //
        NormalizeTotal.FUNCTION.apply(mahalanobis.leverages_sqrt().map(variogram)), //
        mahalanobis.matrix());
  }
}

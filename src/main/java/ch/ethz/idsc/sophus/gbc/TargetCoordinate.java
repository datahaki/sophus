// code by jph
package ch.ethz.idsc.sophus.gbc;

import java.io.Serializable;

import ch.ethz.idsc.sophus.hs.HsDesign;
import ch.ethz.idsc.sophus.hs.Mahalanobis;
import ch.ethz.idsc.sophus.hs.VectorLogManifold;
import ch.ethz.idsc.sophus.math.NormalizeTotal;
import ch.ethz.idsc.tensor.Tensor;
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
/* package */ class TargetCoordinate implements BarycentricCoordinate, Serializable {
  private static final long serialVersionUID = -8582272887789104693L;
  // ---
  private final VectorLogManifold vectorLogManifold;
  private final ScalarUnaryOperator variogram;

  public TargetCoordinate(VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram) {
    this.vectorLogManifold = vectorLogManifold;
    this.variogram = variogram;
  }

  @Override // from BarycentricCoordinate
  public Tensor weights(Tensor sequence, Tensor point) {
    Tensor matrix = new HsDesign(vectorLogManifold).matrix(sequence, point);
    Mahalanobis mahalanobis = new Mahalanobis(matrix);
    return StaticHelper.barycentric( //
        NormalizeTotal.FUNCTION.apply(mahalanobis.leverages_sqrt().map(variogram)), //
        mahalanobis.matrix());
  }
}

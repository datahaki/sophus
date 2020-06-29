// code by jph
package ch.ethz.idsc.sophus.gbc;

import java.io.Serializable;

import ch.ethz.idsc.sophus.hs.VectorLogManifold;
import ch.ethz.idsc.sophus.krg.Mahalanobis;
import ch.ethz.idsc.sophus.krg.Mahalanobis.Form;
import ch.ethz.idsc.sophus.krg.TargetDistances;
import ch.ethz.idsc.sophus.math.NormalizeTotal;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.sca.ScalarUnaryOperator;

/** target coordinate is identical to anchor coordinate
 * 
 * @see AnchorCoordinate
 * @see TargetDistances */
public class TargetCoordinate implements BarycentricCoordinate, Serializable {
  /** @param vectorLogManifold
   * @param variogram
   * @return */
  public static BarycentricCoordinate of(VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram) {
    return new TargetCoordinate(vectorLogManifold, variogram);
  }

  /***************************************************/
  private final Mahalanobis mahalanobis;
  private final ScalarUnaryOperator variogram;

  private TargetCoordinate(VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram) {
    mahalanobis = new Mahalanobis(vectorLogManifold);
    this.variogram = variogram;
  }

  @Override // from BarycentricCoordinate
  public Tensor weights(Tensor sequence, Tensor point) {
    Form form = mahalanobis.new Form(sequence, point);
    return StaticHelper.barycentric( //
        NormalizeTotal.FUNCTION.apply(form.leverage(variogram)), //
        form.levers());
  }
}

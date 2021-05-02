// code by jph
package ch.alpine.sophus.gbc;

import ch.alpine.sophus.hs.HsDesign;
import ch.alpine.sophus.hs.VectorLogManifold;
import ch.alpine.sophus.itp.Kriging;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.TensorUnaryOperator;

public class KrigingCoordinate implements TensorUnaryOperator, VectorField {
  /** @param tensorUnaryOperator
   * @param vectorLogManifold
   * @param sequence
   * @return */
  public static TensorUnaryOperator of( //
      TensorUnaryOperator tensorUnaryOperator, VectorLogManifold vectorLogManifold, Tensor sequence) {
    return new KrigingCoordinate(tensorUnaryOperator, vectorLogManifold, sequence);
  }

  /***************************************************/
  private final HsDesign hsDesign;
  private final Kriging kriging;
  private final Tensor sequence;

  private KrigingCoordinate( //
      TensorUnaryOperator tensorUnaryOperator, VectorLogManifold vectorLogManifold, Tensor sequence) {
    hsDesign = new HsDesign(vectorLogManifold);
    this.kriging = Kriging.barycentric(tensorUnaryOperator, sequence);
    this.sequence = sequence;
  }

  @Override
  public Tensor apply(Tensor point) {
    return StaticHelper.barycentric( //
        hsDesign.matrix(sequence, point), //
        kriging.estimate(point));
  }
}

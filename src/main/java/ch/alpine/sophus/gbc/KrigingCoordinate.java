// code by jph
package ch.alpine.sophus.gbc;

import ch.alpine.sophus.hs.HsDesign;
import ch.alpine.sophus.hs.Manifold;
import ch.alpine.sophus.itp.Kriging;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.TensorUnaryOperator;

public class KrigingCoordinate implements TensorUnaryOperator {
  /** @param tensorUnaryOperator
   * @param manifold
   * @param sequence
   * @return */
  public static TensorUnaryOperator of( //
      TensorUnaryOperator tensorUnaryOperator, Manifold manifold, Tensor sequence) {
    return new KrigingCoordinate(tensorUnaryOperator, manifold, sequence);
  }

  // ---
  private final HsDesign hsDesign;
  private final Kriging kriging;
  private final Tensor sequence;

  private KrigingCoordinate( //
      TensorUnaryOperator tensorUnaryOperator, Manifold manifold, Tensor sequence) {
    hsDesign = new HsDesign(manifold);
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

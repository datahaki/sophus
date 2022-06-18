// code by jph
package ch.alpine.sophus.gbc;

import java.util.Objects;

import ch.alpine.sophus.hs.HsDesign;
import ch.alpine.sophus.itp.Kriging;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.TensorUnaryOperator;

public class KrigingCoordinate implements TensorUnaryOperator {
  /** @param tensorUnaryOperator
   * @param hsDesign
   * @param sequence
   * @return */
  public static TensorUnaryOperator of( //
      TensorUnaryOperator tensorUnaryOperator, HsDesign hsDesign, Tensor sequence) {
    return new KrigingCoordinate(tensorUnaryOperator, hsDesign, sequence);
  }

  // ---
  private final HsDesign hsDesign;
  private final Kriging kriging;
  private final Tensor sequence;

  private KrigingCoordinate( //
      TensorUnaryOperator tensorUnaryOperator, HsDesign hsDesign, Tensor sequence) {
    this.hsDesign = Objects.requireNonNull(hsDesign);
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

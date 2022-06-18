// code by jph
package ch.alpine.sophus.dv;

import java.util.Objects;

import ch.alpine.sophus.hs.HsDesign;
import ch.alpine.sophus.hs.Sedarim;
import ch.alpine.sophus.itp.Kriging;
import ch.alpine.tensor.Tensor;

public class KrigingCoordinate implements Sedarim {
  /** @param tensorUnaryOperator
   * @param hsDesign
   * @param sequence
   * @return */
  public static Sedarim of( //
      Sedarim tensorUnaryOperator, HsDesign hsDesign, Tensor sequence) {
    return new KrigingCoordinate(tensorUnaryOperator, hsDesign, sequence);
  }

  // ---
  private final HsDesign hsDesign;
  private final Kriging kriging;
  private final Tensor sequence;

  private KrigingCoordinate( //
      Sedarim tensorUnaryOperator, HsDesign hsDesign, Tensor sequence) {
    this.hsDesign = Objects.requireNonNull(hsDesign);
    this.kriging = Kriging.barycentric(tensorUnaryOperator, sequence);
    this.sequence = sequence;
  }

  @Override
  public Tensor sunder(Tensor point) {
    return StaticHelper.barycentric( //
        hsDesign.matrix(sequence, point), //
        kriging.estimate(point));
  }
}

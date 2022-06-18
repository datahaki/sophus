// code by jph
package ch.alpine.sophus.dv;

import java.util.Objects;

import ch.alpine.sophus.hs.HsDesign;
import ch.alpine.sophus.hs.Sedarim;
import ch.alpine.sophus.itp.Kriging;
import ch.alpine.tensor.Tensor;

public class KrigingCoordinate implements Sedarim {
  private final HsDesign hsDesign;
  private final Kriging kriging;
  private final Tensor sequence;

  /** @param sedarim
   * @param hsDesign
   * @param sequence */
  public KrigingCoordinate(Sedarim sedarim, HsDesign hsDesign, Tensor sequence) {
    this.hsDesign = Objects.requireNonNull(hsDesign);
    this.kriging = Kriging.barycentric(sedarim, sequence);
    this.sequence = sequence;
  }

  @Override
  public Tensor sunder(Tensor point) {
    return StaticHelper.barycentric( //
        hsDesign.matrix(sequence, point), //
        kriging.estimate(point));
  }
}

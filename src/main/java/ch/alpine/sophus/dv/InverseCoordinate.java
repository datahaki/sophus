// code by jph
package ch.alpine.sophus.dv;

import ch.alpine.sophus.hs.HsDesign;
import ch.alpine.sophus.hs.Sedarim;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.mat.SymmetricMatrixQ;
import ch.alpine.tensor.mat.re.Inverse;

public class InverseCoordinate implements Sedarim {
  private final HsDesign hsDesign;
  private final Sedarim sedarim;
  private final Tensor weights;
  private final Tensor sequence;

  /** @param hsDesign
   * @param sedarim
   * @param sequence */
  public InverseCoordinate(HsDesign hsDesign, Sedarim sedarim, Tensor sequence) {
    this.hsDesign = hsDesign;
    this.sedarim = sedarim;
    Tensor vardst = SymmetricMatrixQ.require(Tensor.of(sequence.stream().map(sedarim::sunder)));
    weights = Inverse.of(vardst);
    this.sequence = sequence;
  }

  @Override
  public Tensor sunder(Tensor point) {
    return StaticHelper.barycentric( //
        hsDesign.matrix(sequence, point), //
        sedarim.sunder(point).dot(weights));
  }
}

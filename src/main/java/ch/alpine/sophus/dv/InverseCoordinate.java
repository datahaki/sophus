// code by jph
package ch.alpine.sophus.dv;

import ch.alpine.sophus.hs.HsDesign;
import ch.alpine.sophus.hs.Sedarim;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.mat.SymmetricMatrixQ;
import ch.alpine.tensor.mat.re.Inverse;

public class InverseCoordinate implements Sedarim {
  /** @param tensorUnaryOperator
   * @param manifold
   * @param sequence
   * @return */
  public static Sedarim of( //
      Sedarim tensorUnaryOperator, HsDesign hsDesign, Tensor sequence) {
    return new InverseCoordinate(tensorUnaryOperator, hsDesign, sequence);
  }

  // ---
  private final Sedarim tensorUnaryOperator;
  private final Tensor weights;
  private final HsDesign hsDesign;
  private final Tensor sequence;

  private InverseCoordinate( //
      Sedarim tensorUnaryOperator, HsDesign hsDesign, Tensor sequence) {
    this.hsDesign = hsDesign;
    this.tensorUnaryOperator = tensorUnaryOperator;
    Tensor vardst = SymmetricMatrixQ.require(Tensor.of(sequence.stream().map(tensorUnaryOperator::sunder)));
    weights = Inverse.of(vardst);
    this.sequence = sequence;
  }

  @Override
  public Tensor sunder(Tensor point) {
    return StaticHelper.barycentric( //
        hsDesign.matrix(sequence, point), //
        // TODO SOPHUS check! ?
        tensorUnaryOperator.sunder(point).dot(weights));
  }
}

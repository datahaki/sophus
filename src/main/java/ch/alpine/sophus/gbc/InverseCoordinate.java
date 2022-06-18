// code by jph
package ch.alpine.sophus.gbc;

import ch.alpine.sophus.hs.HsDesign;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.TensorUnaryOperator;
import ch.alpine.tensor.mat.SymmetricMatrixQ;
import ch.alpine.tensor.mat.re.Inverse;

public class InverseCoordinate implements TensorUnaryOperator {
  /** @param tensorUnaryOperator
   * @param manifold
   * @param sequence
   * @return */
  public static TensorUnaryOperator of( //
      TensorUnaryOperator tensorUnaryOperator, HsDesign hsDesign, Tensor sequence) {
    return new InverseCoordinate(tensorUnaryOperator, hsDesign, sequence);
  }

  // ---
  private final TensorUnaryOperator tensorUnaryOperator;
  private final Tensor weights;
  private final HsDesign hsDesign;
  private final Tensor sequence;

  private InverseCoordinate( //
      TensorUnaryOperator tensorUnaryOperator, HsDesign hsDesign, Tensor sequence) {
    this.hsDesign = hsDesign;
    this.tensorUnaryOperator = tensorUnaryOperator;
    Tensor vardst = SymmetricMatrixQ.require(Tensor.of(sequence.stream().map(tensorUnaryOperator)));
    weights = Inverse.of(vardst);
    this.sequence = sequence;
  }

  @Override
  public Tensor apply(Tensor point) {
    return StaticHelper.barycentric( //
        hsDesign.matrix(sequence, point), //
        tensorUnaryOperator.apply(point).dot(weights));
  }
}

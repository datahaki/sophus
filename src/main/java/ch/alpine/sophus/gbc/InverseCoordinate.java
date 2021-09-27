// code by jph
package ch.alpine.sophus.gbc;

import ch.alpine.sophus.hs.HsDesign;
import ch.alpine.sophus.hs.VectorLogManifold;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.TensorUnaryOperator;
import ch.alpine.tensor.mat.Inverse;
import ch.alpine.tensor.mat.SymmetricMatrixQ;

public class InverseCoordinate implements TensorUnaryOperator, VectorField {
  /** @param tensorUnaryOperator
   * @param vectorLogManifold
   * @param sequence
   * @return */
  public static TensorUnaryOperator of(TensorUnaryOperator tensorUnaryOperator, VectorLogManifold vectorLogManifold, Tensor sequence) {
    return new InverseCoordinate(tensorUnaryOperator, vectorLogManifold, sequence);
  }

  // ---
  private final TensorUnaryOperator tensorUnaryOperator;
  private final Tensor weights;
  private final HsDesign hsDesign;
  private final Tensor sequence;

  private InverseCoordinate(TensorUnaryOperator tensorUnaryOperator, VectorLogManifold vectorLogManifold, Tensor sequence) {
    hsDesign = new HsDesign(vectorLogManifold);
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

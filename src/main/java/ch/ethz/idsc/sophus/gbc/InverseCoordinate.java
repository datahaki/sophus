// code by jph
package ch.ethz.idsc.sophus.gbc;

import ch.ethz.idsc.sophus.hs.HsDesign;
import ch.ethz.idsc.sophus.hs.VectorLogManifold;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.api.TensorUnaryOperator;
import ch.ethz.idsc.tensor.mat.Inverse;
import ch.ethz.idsc.tensor.mat.SymmetricMatrixQ;

public class InverseCoordinate implements TensorUnaryOperator, VectorField {
  private static final long serialVersionUID = -4923137024335634245L;

  /** @param tensorUnaryOperator
   * @param vectorLogManifold
   * @param sequence
   * @return */
  public static TensorUnaryOperator of(TensorUnaryOperator tensorUnaryOperator, VectorLogManifold vectorLogManifold, Tensor sequence) {
    return new InverseCoordinate(tensorUnaryOperator, vectorLogManifold, sequence);
  }

  /***************************************************/
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
        tensorUnaryOperator.apply(point).dot(weights), //
        hsDesign.matrix(sequence, point));
  }
}

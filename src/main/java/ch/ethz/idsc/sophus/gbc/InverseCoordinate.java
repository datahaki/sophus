// code by jph
package ch.ethz.idsc.sophus.gbc;

import ch.ethz.idsc.sophus.hs.HsProjection;
import ch.ethz.idsc.sophus.hs.VectorLogManifold;
import ch.ethz.idsc.sophus.krg.BiinvariantVector;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.mat.Inverse;
import ch.ethz.idsc.tensor.mat.SymmetricMatrixQ;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;

public class InverseCoordinate implements TensorUnaryOperator {
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
  private final HsProjection hsProjection;
  private final Tensor sequence;

  private InverseCoordinate(TensorUnaryOperator tensorUnaryOperator, VectorLogManifold vectorLogManifold, Tensor sequence) {
    this.tensorUnaryOperator = tensorUnaryOperator;
    Tensor vardst = SymmetricMatrixQ.require(Tensor.of(sequence.stream().map(tensorUnaryOperator)));
    weights = Inverse.of(vardst);
    hsProjection = new HsProjection(vectorLogManifold);
    this.sequence = sequence;
  }

  @Override
  public Tensor apply(Tensor point) {
    return new BiinvariantVector( //
        hsProjection.projection(sequence, point), //
        tensorUnaryOperator.apply(point).dot(weights)).coordinate();
  }
}

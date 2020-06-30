// code by jph
package ch.ethz.idsc.sophus.gbc;

import ch.ethz.idsc.sophus.hs.HsLevers;
import ch.ethz.idsc.sophus.hs.VectorLogManifold;
import ch.ethz.idsc.sophus.krg.Kriging;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;

public class KrigingCoordinate implements TensorUnaryOperator {
  /** @param tensorUnaryOperator
   * @param vectorLogManifold
   * @param sequence
   * @return */
  public static TensorUnaryOperator of(TensorUnaryOperator tensorUnaryOperator, VectorLogManifold vectorLogManifold, Tensor sequence) {
    return new KrigingCoordinate(tensorUnaryOperator, vectorLogManifold, sequence);
  }

  /***************************************************/
  private final HsLevers hsLevers;
  private final Kriging kriging;
  private final Tensor sequence;

  private KrigingCoordinate(TensorUnaryOperator tensorUnaryOperator, VectorLogManifold vectorLogManifold, Tensor sequence) {
    hsLevers = new HsLevers(vectorLogManifold);
    this.kriging = Kriging.barycentric(tensorUnaryOperator, sequence);
    this.sequence = sequence;
  }

  @Override
  public Tensor apply(Tensor point) {
    return StaticHelper.barycentric( //
        kriging.estimate(point), //
        hsLevers.levers(sequence, point));
  }
}

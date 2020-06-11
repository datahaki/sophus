// code by jph
package ch.ethz.idsc.sophus.gbc;

import ch.ethz.idsc.sophus.hs.HsProjection;
import ch.ethz.idsc.sophus.hs.VectorLogManifold;
import ch.ethz.idsc.sophus.krg.BiinvariantVector;
import ch.ethz.idsc.sophus.krg.Kriging;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;

public class KrigingCoordinate implements TensorUnaryOperator {
  public static TensorUnaryOperator of(TensorUnaryOperator tensorUnaryOperator, VectorLogManifold vectorLogManifold, Tensor sequence) {
    return new KrigingCoordinate(tensorUnaryOperator, vectorLogManifold, sequence);
  }

  /***************************************************/
  private final Kriging kriging;
  private final HsProjection hsProjection;
  private final Tensor sequence;

  private KrigingCoordinate(TensorUnaryOperator tensorUnaryOperator, VectorLogManifold vectorLogManifold, Tensor sequence) {
    this.kriging = Kriging.barycentric(tensorUnaryOperator, sequence);
    hsProjection = new HsProjection(vectorLogManifold);
    this.sequence = sequence;
  }

  @Override
  public Tensor apply(Tensor point) {
    return new BiinvariantVector( //
        hsProjection.projection(sequence, point), //
        kriging.estimate(point)).coordinate();
  }
}

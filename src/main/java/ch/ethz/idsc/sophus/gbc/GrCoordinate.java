// code by jph
package ch.ethz.idsc.sophus.gbc;

import ch.ethz.idsc.sophus.hs.HsProjection;
import ch.ethz.idsc.sophus.hs.VectorLogManifold;
import ch.ethz.idsc.sophus.krg.InversePowerVariogram;
import ch.ethz.idsc.sophus.math.NormalizeTotal;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;
import ch.ethz.idsc.tensor.red.Frobenius;
import ch.ethz.idsc.tensor.sca.ScalarUnaryOperator;

/** biinvariant coordinate
 * 
 * @see InversePowerVariogram */
public class GrCoordinate implements TensorUnaryOperator {
  private final HsProjection hsProjection;
  private final ScalarUnaryOperator variogram;
  private final Tensor sequence;
  private final Tensor grassmann;

  /** @param vectorLogManifold
   * @param variogram for instance power to minus two
   * @param sequence */
  public GrCoordinate(VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram, Tensor sequence) {
    hsProjection = new HsProjection(vectorLogManifold);
    this.variogram = variogram;
    this.sequence = sequence;
    grassmann = Tensor.of(sequence.stream().map(point -> hsProjection.projection(sequence, point)));
  }

  @Override
  public Tensor apply(Tensor point) {
    Tensor projection = hsProjection.projection(sequence, point);
    Tensor target = NormalizeTotal.FUNCTION.apply(Tensor.of(grassmann.stream() //
        .map(x -> Frobenius.between(x, projection)) //
        .map(variogram)));
    return NormalizeAffine.fromProjection(target, projection);
  }
}

// code by jph
package ch.ethz.idsc.sophus.krg;

import java.io.Serializable;

import ch.ethz.idsc.sophus.hs.HsProjection;
import ch.ethz.idsc.sophus.hs.VectorLogManifold;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.red.Frobenius;
import ch.ethz.idsc.tensor.sca.ScalarUnaryOperator;

/** @see AbsoluteDistances */
public class Relative2Distances implements Serializable {
  private final HsProjection hsProjection;
  private final ScalarUnaryOperator variogram;
  private final Tensor sequence;
  private final Tensor grassmann;

  /** @param vectorLogManifold
   * @param variogram
   * @param sequence */
  public Relative2Distances(VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram, Tensor sequence) {
    this.hsProjection = new HsProjection(vectorLogManifold);
    this.variogram = variogram;
    this.sequence = sequence;
    grassmann = Tensor.of(sequence.stream() //
        .map(point -> hsProjection.projection(sequence, point)));
  }

  public BiinvariantVector biinvariantVector(Tensor point) {
    Tensor projection = hsProjection.projection(sequence, point);
    return new BiinvariantVector(projection, Tensor.of(grassmann.stream() //
        .map(x -> Frobenius.between(x, projection)) //
        .map(variogram)));
  }
}

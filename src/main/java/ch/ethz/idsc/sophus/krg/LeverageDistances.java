// code by jph
package ch.ethz.idsc.sophus.krg;

import java.io.Serializable;
import java.util.Objects;

import ch.ethz.idsc.sophus.hs.HsProjection;
import ch.ethz.idsc.sophus.hs.VectorLogManifold;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.red.Diagonal;
import ch.ethz.idsc.tensor.sca.Clips;
import ch.ethz.idsc.tensor.sca.ScalarUnaryOperator;
import ch.ethz.idsc.tensor.sca.Sqrt;

/** @see TargetDistances */
public class LeverageDistances implements Serializable {
  private final HsProjection hsProjection;
  private final ScalarUnaryOperator variogram;

  /** @param vectorLogManifold
   * @param variogram */
  public LeverageDistances(VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram) {
    this.hsProjection = new HsProjection(vectorLogManifold);
    this.variogram = Objects.requireNonNull(variogram);
  }

  public BiinvariantVector biinvariantVector(Tensor sequence, Tensor point) {
    Tensor influence = hsProjection.influence(sequence, point); // hat matrix
    return new BiinvariantVector(influence, //
        Tensor.of(Diagonal.of(influence).stream() // stream of leverages
            .map(Scalar.class::cast) //
            .map(Clips.unit()) // theory asserts that leverage is in [0, 1]
            .map(Sqrt.FUNCTION) //
            .map(variogram))); //
  }
}
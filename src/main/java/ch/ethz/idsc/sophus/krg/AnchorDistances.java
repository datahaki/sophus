// code by jph
package ch.ethz.idsc.sophus.krg;

import java.io.Serializable;
import java.util.Objects;

import ch.ethz.idsc.sophus.hs.HsProjection;
import ch.ethz.idsc.sophus.hs.VectorLogManifold;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.red.Diagonal;
import ch.ethz.idsc.tensor.sca.Abs;
import ch.ethz.idsc.tensor.sca.ScalarUnaryOperator;
import ch.ethz.idsc.tensor.sca.Sqrt;

public class AnchorDistances implements Serializable {
  private static final Scalar ONE = RealScalar.of(1.0);
  // ---
  private final HsProjection hsProjection;
  private final ScalarUnaryOperator variogram;

  /** @param vectorLogManifold
   * @param variogram */
  public AnchorDistances(VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram) {
    this.hsProjection = new HsProjection(vectorLogManifold);
    this.variogram = Objects.requireNonNull(variogram);
  }

  public BiinvariantVector biinvariantVector(Tensor sequence, Tensor point) {
    Tensor projection = hsProjection.projection(sequence, point);
    return new BiinvariantVector(projection, //
        Tensor.of(Diagonal.of(projection).stream() //
            .map(Scalar.class::cast) //
            .map(v -> Abs.between(v, ONE)) // theory asserts that v in [0, 1]
            .map(Sqrt.FUNCTION) //
            .map(variogram))); //
  }
}

// code by jph
package ch.ethz.idsc.sophus.krg;

import java.io.Serializable;

import ch.ethz.idsc.sophus.hs.HsProjection;
import ch.ethz.idsc.sophus.hs.VectorLogManifold;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.red.Diagonal;
import ch.ethz.idsc.tensor.sca.Abs;
import ch.ethz.idsc.tensor.sca.ScalarUnaryOperator;
import ch.ethz.idsc.tensor.sca.Sqrt;

/** @see AbsoluteDistances */
public class Relative1Distances implements Serializable {
  private static final Scalar ONE = RealScalar.of(1.0);
  // ---
  private final HsProjection hsProjection;
  private final ScalarUnaryOperator variogram;

  /** @param vectorLogManifold
   * @param variogram */
  public Relative1Distances(VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram) {
    this.hsProjection = new HsProjection(vectorLogManifold);
    this.variogram = variogram;
  }

  public BiinvariantVector biinvariantVector(Tensor sequence, Tensor point) {
    Tensor projection = hsProjection.projection(sequence, point);
    return new BiinvariantVector(projection, //
        Tensor.of(Diagonal.of(projection).stream() //
            .map(Scalar.class::cast) //
            .map(v -> Abs.between(v, ONE)) //
            .map(Sqrt.FUNCTION) //
            .map(variogram))); //
  }
}

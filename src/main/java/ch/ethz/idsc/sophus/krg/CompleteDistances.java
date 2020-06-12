// code by jph
package ch.ethz.idsc.sophus.krg;

import java.io.Serializable;

import ch.ethz.idsc.sophus.hs.HsProjection;
import ch.ethz.idsc.sophus.hs.VectorLogManifold;
import ch.ethz.idsc.sophus.hs.gr.GrMetric;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.red.Frobenius;
import ch.ethz.idsc.tensor.sca.ScalarUnaryOperator;

public abstract class CompleteDistances implements Serializable {
  /** @param vectorLogManifold
   * @param variogram
   * @param sequence
   * @return */
  public static CompleteDistances frobenius(VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram, Tensor sequence) {
    return new CompleteDistances(vectorLogManifold, variogram, sequence) {
      @Override
      protected Scalar distance(Tensor x, Tensor projection) {
        return Frobenius.between(x, projection);
      }
    };
  }

  /** @param vectorLogManifold
   * @param variogram
   * @param sequence
   * @return */
  public static CompleteDistances geodesic(VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram, Tensor sequence) {
    return new CompleteDistances(vectorLogManifold, variogram, sequence) {
      @Override
      protected Scalar distance(Tensor x, Tensor projection) {
        return GrMetric.INSTANCE.distance(x, projection);
      }
    };
  }

  /***************************************************/
  private final HsProjection hsProjection;
  private final ScalarUnaryOperator variogram;
  private final Tensor sequence;
  private final Tensor grassmann;

  /** @param vectorLogManifold
   * @param variogram
   * @param sequence
   * @param tensorMetric */
  private CompleteDistances(VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram, Tensor sequence) {
    this.hsProjection = new HsProjection(vectorLogManifold);
    this.variogram = variogram;
    this.sequence = sequence;
    grassmann = Tensor.of(sequence.stream().map(point -> hsProjection.projection(sequence, point)));
  }

  public BiinvariantVector biinvariantVector(Tensor point) {
    Tensor projection = hsProjection.projection(sequence, point);
    return new BiinvariantVector(projection, Tensor.of(grassmann.stream() //
        .map(x -> distance(x, projection)) //
        .map(variogram)));
  }

  protected abstract Scalar distance(Tensor x, Tensor projection);
}

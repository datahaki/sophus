// code by jph
package ch.ethz.idsc.sophus.krg;

import java.io.Serializable;

import ch.ethz.idsc.sophus.hs.HsProjection;
import ch.ethz.idsc.sophus.hs.VectorLogManifold;
import ch.ethz.idsc.sophus.hs.gr.GrMetric;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.red.Frobenius;
import ch.ethz.idsc.tensor.red.Norm;
import ch.ethz.idsc.tensor.sca.ScalarUnaryOperator;

/** for Rn and Sn the frobenius distance results in identical coordinates as the 2-norm distance
 * 
 * however, for SE(2) the frobenius and 2-norm coordinates do not match! */
public abstract class HarborDistances implements Serializable {
  /** @param vectorLogManifold
   * @param variogram
   * @param sequence
   * @return */
  public static HarborDistances frobenius(VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram, Tensor sequence) {
    return new HarborDistances(vectorLogManifold, variogram, sequence) {
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
  public static HarborDistances norm2(VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram, Tensor sequence) {
    return new HarborDistances(vectorLogManifold, variogram, sequence) {
      @Override
      protected Scalar distance(Tensor x, Tensor projection) {
        return Norm._2.ofMatrix(x.subtract(projection));
      }
    };
  }

  /** @param vectorLogManifold
   * @param variogram
   * @param sequence
   * @return */
  public static HarborDistances geodesic(VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram, Tensor sequence) {
    return new HarborDistances(vectorLogManifold, variogram, sequence) {
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
  private HarborDistances(VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram, Tensor sequence) {
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

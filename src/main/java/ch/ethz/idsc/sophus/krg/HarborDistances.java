// code by jph
package ch.ethz.idsc.sophus.krg;

import java.io.Serializable;

import ch.ethz.idsc.sophus.hs.HsProjection;
import ch.ethz.idsc.sophus.hs.HsProjection.Matrix;
import ch.ethz.idsc.sophus.hs.VectorLogManifold;
import ch.ethz.idsc.sophus.hs.gr.GrMetric;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.red.Diagonal;
import ch.ethz.idsc.tensor.red.Frobenius;
import ch.ethz.idsc.tensor.red.Norm;

/** for Rn and Sn the frobenius distance results in identical coordinates as the 2-norm distance
 * 
 * however, for SE(2) the frobenius and 2-norm coordinates do not match!
 * 
 * Reference:
 * "Biinvariant Distance Vectors"
 * by Jan Hakenberg, 2020 */
public abstract class HarborDistances implements Serializable {
  /** @param vectorLogManifold
   * @param sequence
   * @return */
  public static HarborDistances frobenius(VectorLogManifold vectorLogManifold, Tensor sequence) {
    return new HarborDistances(vectorLogManifold, sequence) {
      @Override
      protected Scalar distance(Tensor x, Tensor projection) {
        return Frobenius.between(x, projection);
      }
    };
  }

  public static HarborDistances diagonal(VectorLogManifold vectorLogManifold, Tensor sequence) {
    return new HarborDistances(vectorLogManifold, sequence) {
      @Override
      protected Scalar distance(Tensor x, Tensor projection) {
        return Norm._2.between(Diagonal.of(x), Diagonal.of(projection));
      }
    };
  }

  /** @param vectorLogManifold
   * @param sequence
   * @return */
  public static HarborDistances norm2(VectorLogManifold vectorLogManifold, Tensor sequence) {
    return new HarborDistances(vectorLogManifold, sequence) {
      @Override
      protected Scalar distance(Tensor x, Tensor projection) {
        return Norm._2.ofMatrix(x.subtract(projection));
      }
    };
  }

  /** @param vectorLogManifold
   * @param sequence
   * @return */
  public static HarborDistances geodesic(VectorLogManifold vectorLogManifold, Tensor sequence) {
    return new HarborDistances(vectorLogManifold, sequence) {
      @Override
      protected Scalar distance(Tensor x, Tensor projection) {
        return GrMetric.INSTANCE.distance(x, projection);
      }
    };
  }

  /***************************************************/
  private final HsProjection hsProjection;
  private final Tensor sequence;
  private final Tensor grassmann;

  /** @param vectorLogManifold
   * @param sequence */
  private HarborDistances(VectorLogManifold vectorLogManifold, Tensor sequence) {
    this.hsProjection = new HsProjection(vectorLogManifold);
    this.sequence = sequence;
    grassmann = Tensor.of(sequence.stream().map(point -> hsProjection.new Matrix(sequence, point).influence()));
  }

  public BiinvariantVector biinvariantVector(Tensor point) {
    Matrix matrix = hsProjection.new Matrix(sequence, point);
    return new BiinvariantVector( //
        matrix.influence(), //
        Tensor.of(grassmann.stream().map(x -> distance(x, matrix.influence()))));
  }

  protected abstract Scalar distance(Tensor x, Tensor projection);
}

// code by jph
package ch.ethz.idsc.sophus.krg;

import java.io.Serializable;

import ch.ethz.idsc.sophus.hs.HsInfluence;
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
  private final VectorLogManifold vectorLogManifold;
  private final Tensor sequence;
  private final Tensor influence;

  /** @param vectorLogManifold
   * @param sequence */
  private HarborDistances(VectorLogManifold vectorLogManifold, Tensor sequence) {
    this.vectorLogManifold = vectorLogManifold;
    this.sequence = sequence;
    influence = Tensor.of(sequence.stream() //
        .map(vectorLogManifold::logAt) //
        .map(tangentSpace -> new HsInfluence(tangentSpace, sequence)) //
        .map(HsInfluence::matrix));
  }

  /** @param point
   * @return */
  public BiinvariantVector biinvariantVector(Tensor point) {
    HsInfluence hsInfluence = new HsInfluence(vectorLogManifold.logAt(point), sequence);
    return new BiinvariantVector( //
        hsInfluence, //
        Tensor.of(influence.stream().map(x -> distance(x, hsInfluence.matrix()))));
  }

  /** @param x
   * @param projection
   * @return */
  protected abstract Scalar distance(Tensor x, Tensor projection);
}

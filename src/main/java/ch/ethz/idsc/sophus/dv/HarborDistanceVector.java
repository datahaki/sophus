// code by jph
package ch.ethz.idsc.sophus.dv;

import java.io.Serializable;

import ch.ethz.idsc.sophus.hs.BiinvariantVector;
import ch.ethz.idsc.sophus.hs.HsDesign;
import ch.ethz.idsc.sophus.hs.VectorLogManifold;
import ch.ethz.idsc.sophus.hs.gr.GrMetric;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.mat.InfluenceMatrix;
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
public abstract class HarborDistanceVector implements Serializable {
  private static final long serialVersionUID = 7613766574684059655L;

  /** @param vectorLogManifold
   * @param sequence
   * @return */
  public static HarborDistanceVector frobenius(VectorLogManifold vectorLogManifold, Tensor sequence) {
    return new HarborDistanceVector(vectorLogManifold, sequence) {
      private static final long serialVersionUID = 3224364263932389466L;

      @Override
      protected Scalar distance(Tensor x, Tensor projection) {
        return Frobenius.between(x, projection);
      }
    };
  }

  public static HarborDistanceVector diagonal(VectorLogManifold vectorLogManifold, Tensor sequence) {
    return new HarborDistanceVector(vectorLogManifold, sequence) {
      private static final long serialVersionUID = -5120046492198795006L;

      @Override
      protected Scalar distance(Tensor x, Tensor projection) {
        return Norm._2.between(Diagonal.of(x), Diagonal.of(projection));
      }
    };
  }

  /** @param vectorLogManifold
   * @param sequence
   * @return */
  public static HarborDistanceVector norm2(VectorLogManifold vectorLogManifold, Tensor sequence) {
    return new HarborDistanceVector(vectorLogManifold, sequence) {
      private static final long serialVersionUID = 4219012689582124465L;

      @Override
      protected Scalar distance(Tensor x, Tensor projection) {
        return Norm._2.ofMatrix(x.subtract(projection));
      }
    };
  }

  /** @param vectorLogManifold
   * @param sequence
   * @return */
  public static HarborDistanceVector geodesic(VectorLogManifold vectorLogManifold, Tensor sequence) {
    return new HarborDistanceVector(vectorLogManifold, sequence) {
      private static final long serialVersionUID = -2438143026398610720L;

      @Override
      protected Scalar distance(Tensor x, Tensor projection) {
        return GrMetric.INSTANCE.distance(x, projection);
      }
    };
  }

  /***************************************************/
  private final HsDesign hsDesign;
  private final Tensor sequence;
  private final Tensor influence;

  /** @param vectorLogManifold
   * @param sequence */
  private HarborDistanceVector(VectorLogManifold vectorLogManifold, Tensor sequence) {
    hsDesign = new HsDesign(vectorLogManifold);
    this.sequence = sequence;
    influence = Tensor.of(sequence.stream() //
        .map(point -> hsDesign.matrix(sequence, point)) //
        .map(InfluenceMatrix::of) //
        .map(InfluenceMatrix::matrix));
  }

  /** @param point
   * @return */
  public BiinvariantVector biinvariantVector(Tensor point) {
    Tensor design = hsDesign.matrix(sequence, point);
    InfluenceMatrix influenceMatrix = InfluenceMatrix.of(design);
    Tensor matrix = influenceMatrix.matrix();
    return new BiinvariantVector( //
        influenceMatrix, //
        Tensor.of(influence.stream().map(x -> distance(x, matrix))));
  }

  /** @param x
   * @param projection
   * @return */
  protected abstract Scalar distance(Tensor x, Tensor projection);
}

// code by jph
package ch.ethz.idsc.sophus.krg;

import ch.ethz.idsc.sophus.hs.VectorLogManifold;
import ch.ethz.idsc.sophus.krg.Mahalanobis.Form;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;
import ch.ethz.idsc.tensor.sca.ScalarUnaryOperator;
import ch.ethz.idsc.tensor.sca.Sqrt;

/** computes form at given point based on points in sequence and returns
 * vector of evaluations dMah_x(p_i) of points in sequence.
 * 
 * <p>Reference:
 * "Exponential Barycenters of the Canonical Cartan Connection and Invariant Means on Lie Groups"
 * by Xavier Pennec, Vincent Arsigny, 2012, p. 39
 * 
 * @see DiagonalDistances */
public class Mahalanobis1Distances implements TensorUnaryOperator {
  /** @param vectorLogManifold
   * @param variogram
   * @param sequence
   * @return */
  public static TensorUnaryOperator of(VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram, Tensor sequence) {
    return new Mahalanobis1Distances(vectorLogManifold, variogram, sequence);
  }

  /***************************************************/
  private final Mahalanobis mahalanobis;
  private final ScalarUnaryOperator variogram;
  private final Tensor sequence;

  private Mahalanobis1Distances(VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram, Tensor sequence) {
    mahalanobis = new Mahalanobis(vectorLogManifold);
    this.variogram = variogram;
    this.sequence = sequence;
  }

  @Override
  public Tensor apply(Tensor point) {
    Form form = mahalanobis.new Form(sequence, point);
    Tensor bi = form.inverse();
    return Tensor.of(form.vs().stream() //
        .map(v -> bi.dot(v).dot(v)) //
        .map(Scalar.class::cast) //
        .map(Sqrt.FUNCTION) //
        .map(variogram));
  }
}

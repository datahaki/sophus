// code by jph
package ch.ethz.idsc.sophus.krg;

import ch.ethz.idsc.sophus.hs.VectorLogManifold;
import ch.ethz.idsc.sophus.krg.Mahalanobis.Form;
import ch.ethz.idsc.tensor.RationalScalar;
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
 * @see SolitaryDistances */
public class SolitaryMahalanobisDistances implements TensorUnaryOperator {
  /** @param vectorLogManifold
   * @param variogram
   * @param sequence
   * @return */
  public static TensorUnaryOperator of(VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram, Tensor sequence) {
    return new SolitaryMahalanobisDistances(vectorLogManifold, variogram, sequence);
  }

  /***************************************************/
  private final Mahalanobis mahalanobis;
  private final ScalarUnaryOperator variogram;
  private final Tensor sequence;
  private final Scalar factor;

  private SolitaryMahalanobisDistances(VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram, Tensor sequence) {
    mahalanobis = new Mahalanobis(vectorLogManifold);
    this.variogram = variogram;
    this.sequence = sequence;
    factor = RationalScalar.of(1, sequence.length());
  }

  @Override
  public Tensor apply(Tensor point) {
    Form form = mahalanobis.new Form(sequence, point);
    Tensor bi = form.sigma_inverse().multiply(factor);
    return Tensor.of(form.levers().stream() //
        .map(v -> bi.dot(v).dot(v)) //
        .map(Scalar.class::cast) //
        .map(Sqrt.FUNCTION) //
        .map(variogram));
  }
}

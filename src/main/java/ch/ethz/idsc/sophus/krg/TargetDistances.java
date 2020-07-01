// code by jph
package ch.ethz.idsc.sophus.krg;

import java.io.Serializable;
import java.util.Objects;

import ch.ethz.idsc.sophus.hs.VectorLogManifold;
import ch.ethz.idsc.sophus.math.WeightingInterface;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.sca.ScalarUnaryOperator;

/** computes form at given point based on points in sequence and returns
 * vector of evaluations dMah_x(p_i) of points in sequence.
 * 
 * target distances are identical to anchor distances
 * 
 * <p>Reference:
 * "Exponential Barycenters of the Canonical Cartan Connection and Invariant Means on Lie Groups"
 * by Xavier Pennec, Vincent Arsigny, 2012, p. 39
 * 
 * @see LeverageDistances */
public class TargetDistances implements WeightingInterface, Serializable {
  /** @param vectorLogManifold
   * @param variogram
   * @return */
  public static WeightingInterface of(VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram) {
    return new TargetDistances(vectorLogManifold, Objects.requireNonNull(variogram));
  }

  /***************************************************/
  private final Mahalanobis mahalanobis;
  private final ScalarUnaryOperator variogram;

  private TargetDistances(VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram) {
    mahalanobis = new Mahalanobis(vectorLogManifold);
    this.variogram = variogram;
  }

  @Override // from WeightingInterface
  public Tensor weights(Tensor sequence, Tensor point) {
    return mahalanobis.new Form(sequence, point).leverages(variogram);
  }
}

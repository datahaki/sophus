// code by jph
package ch.ethz.idsc.sophus.lie.so3;

import ch.ethz.idsc.sophus.math.TensorMetric;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.mat.LinearSolve;
import ch.ethz.idsc.tensor.nrm.Vector2Norm;

/** left-invariant Riemannian distance on SO(3)
 * 
 * Reference:
 * "Computing the Mean of Geometric Features Application to the Mean Rotation"
 * by Xavier Pennec, 1998 */
public enum So3Metric implements TensorMetric {
  INSTANCE;

  @Override // from TensorMetric
  public Scalar distance(Tensor p, Tensor q) {
    return Vector2Norm.of(Rodrigues.INSTANCE.vectorLog(LinearSolve.of(p, q)));
  }
}

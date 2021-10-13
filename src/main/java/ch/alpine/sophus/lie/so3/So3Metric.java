// code by jph
package ch.alpine.sophus.lie.so3;

import ch.alpine.sophus.math.TensorMetric;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.mat.re.LinearSolve;
import ch.alpine.tensor.nrm.Vector2Norm;

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

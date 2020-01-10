// code by jph
package ch.ethz.idsc.sophus.lie.so3;

import ch.ethz.idsc.sophus.math.GeodesicInterface;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.mat.LinearSolve;
import ch.ethz.idsc.tensor.opt.ScalarTensorFunction;

public enum So3Geodesic implements GeodesicInterface {
  INSTANCE;
  // ---
  @Override
  public ScalarTensorFunction curve(Tensor p, Tensor q) {
    Tensor log = So3Exponential.INSTANCE.log(LinearSolve.of(p, q));
    return scalar -> p.dot(So3Exponential.INSTANCE.exp(log.multiply(scalar)));
  }

  /** p and q are orthogonal matrices with dimension 3 x 3 */
  @Override // from GeodesicInterface
  public Tensor split(Tensor p, Tensor q, Scalar scalar) {
    return curve(p, q).apply(scalar);
  }
}

// code by jph
package ch.alpine.sophus.lie.so3;

import ch.alpine.sophus.api.GeodesicSpace;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.ScalarTensorFunction;
import ch.alpine.tensor.mat.re.LinearSolve;

/** p and q are orthogonal matrices with dimension 3 x 3 */
public enum So3Geodesic implements GeodesicSpace {
  INSTANCE;

  @Override // from ParametricCurve
  public ScalarTensorFunction curve(Tensor p, Tensor q) {
    Tensor log = Rodrigues.INSTANCE.log(LinearSolve.of(p, q));
    return scalar -> p.dot(Rodrigues.INSTANCE.exp(log.multiply(scalar)));
  }
}

// code by jph
package ch.alpine.sophus.lie.se3;

import ch.alpine.sophus.api.GeodesicSpace;
import ch.alpine.sophus.lie.LieGroupElement;
import ch.alpine.sophus.lie.gl.GlGroup;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.ScalarTensorFunction;
import ch.alpine.tensor.mat.re.LinearSolve;

/** geodesic in special Euclidean group SE(3) of affine transformations
 * 
 * input p and q are 4 x 4 matrices that encode affine transformations
 * 
 * @see GlGroup
 * @see LieGroupElement */
public enum Se3Geodesic implements GeodesicSpace {
  INSTANCE;

  @Override // from TensorGeodesic
  public ScalarTensorFunction curve(Tensor p, Tensor q) {
    Tensor log = Se3Exponential.INSTANCE.log(LinearSolve.of(p, q));
    return scalar -> p.dot(Se3Exponential.INSTANCE.exp(log.multiply(scalar)));
  }

  @Override // from GeodesicInterface
  public Tensor split(Tensor p, Tensor q, Scalar scalar) {
    return curve(p, q).apply(scalar);
  }
}

// code by ob
package ch.alpine.sophus.lie.so2;

import ch.alpine.sophus.api.GeodesicSpace;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.ScalarTensorFunction;

public enum So2Geodesic implements GeodesicSpace {
  INSTANCE;

  @Override // from TensorGeodesic
  public ScalarTensorFunction curve(Tensor p, Tensor q) {
    Tensor log = q.subtract(p);
    return scalar -> p.add(log.multiply(scalar));
  }

  /** p and q are orthogonal matrices with dimension 2 x 2 */
  @Override // from GeodesicInterface
  public Scalar split(Tensor p, Tensor q, Scalar scalar) {
    return (Scalar) curve(p, q).apply(scalar);
  }
}

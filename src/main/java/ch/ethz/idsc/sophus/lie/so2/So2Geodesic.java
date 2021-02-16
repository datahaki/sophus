// code by ob
package ch.ethz.idsc.sophus.lie.so2;

import ch.ethz.idsc.sophus.math.Geodesic;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.api.ScalarTensorFunction;

public enum So2Geodesic implements Geodesic {
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

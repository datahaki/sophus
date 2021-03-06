// code by jph
package ch.ethz.idsc.sophus.hs.s2;

import ch.ethz.idsc.sophus.hs.sn.SnGeodesic;
import ch.ethz.idsc.sophus.lie.so3.Rodrigues;
import ch.ethz.idsc.sophus.math.Geodesic;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Scalars;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.api.ScalarTensorFunction;
import ch.ethz.idsc.tensor.lie.Cross;
import ch.ethz.idsc.tensor.sca.ArcCos;
import ch.ethz.idsc.tensor.sca.Sin;

/** geodesic on 2-dimensional sphere embedded in R^3
 * 
 * https://en.wikipedia.org/wiki/N-sphere
 * 
 * superseded by {@link SnGeodesic} */
public enum S2Geodesic implements Geodesic {
  INSTANCE;

  @Override // from TensorGeodesic
  public ScalarTensorFunction curve(Tensor p, Tensor q) {
    Scalar a = ArcCos.FUNCTION.apply((Scalar) p.dot(q)); // complex number if |p.q| > 1
    Scalar sina = Sin.FUNCTION.apply(a);
    if (Scalars.isZero(sina)) // when p == q or p == -q
      return scalar -> p.copy();
    Scalar prod = a.divide(sina);
    Tensor cross = Cross.of(p, q);
    return scalar -> Rodrigues.vectorExp(cross.multiply(scalar).multiply(prod)).dot(p);
  }

  /** p and q are vectors of length 3 with unit length
   * 
   * Careful: function does not check length of input vectors! */
  @Override // from GeodesicInterface
  public Tensor split(Tensor p, Tensor q, Scalar scalar) {
    return curve(p, q).apply(scalar);
  }
}

// code by jph
package ch.alpine.sophus.hs.s2;

import ch.alpine.sophus.hs.GeodesicSpace;
import ch.alpine.sophus.hs.sn.SnManifold;
import ch.alpine.sophus.lie.so3.Rodrigues;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Scalars;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.ScalarTensorFunction;
import ch.alpine.tensor.lie.Cross;
import ch.alpine.tensor.sca.tri.ArcCos;
import ch.alpine.tensor.sca.tri.Sin;

/** geodesic on 2-dimensional sphere embedded in R^3
 * 
 * https://en.wikipedia.org/wiki/N-sphere
 * 
 * superseded by {@link SnManifold}
 * 
 * p and q are vectors of length 3 with unit length
 * 
 * Careful: function does not check length of input vectors! */
public enum S2Geodesic implements GeodesicSpace {
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
}

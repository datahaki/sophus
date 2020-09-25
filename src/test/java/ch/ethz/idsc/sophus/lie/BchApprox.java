// code by jph
package ch.ethz.idsc.sophus.lie;

import ch.ethz.idsc.tensor.RationalScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.TensorRuntimeException;

/** Reference: Neeb */
public enum BchApprox {
  ;
  public static Tensor of(int degree, Tensor ad, Tensor x, Tensor y) {
    switch (degree) {
    case 0:
      return ap0(ad, x, y);
    case 1:
      return ap1(ad, x, y);
    case 2:
      return ap2(ad, x, y);
    case 3:
      return ap3(ad, x, y);
    }
    throw TensorRuntimeException.of(ad);
  }

  public static Tensor ap0(Tensor ad, Tensor x, Tensor y) {
    return x.add(y);
  }

  public static Tensor ap1(Tensor ad, Tensor x, Tensor y) {
    return ap0(ad, x, y).add(ad.dot(x).dot(y).multiply(RationalScalar.HALF));
  }

  public static Tensor ap2(Tensor ad, Tensor x, Tensor y) {
    Tensor xxy = ad.dot(x).dot(ad.dot(x).dot(y)).multiply(RationalScalar.of(+1, 12));
    Tensor yyx = ad.dot(y).dot(ad.dot(y).dot(x)).multiply(RationalScalar.of(+1, 12));
    return ap1(ad, x, y).add(xxy).add(yyx);
  }

  public static Tensor ap3(Tensor ad, Tensor x, Tensor y) {
    Tensor xyxy = ad.dot(x).dot(ad.dot(y).dot(ad.dot(x).dot(y))).multiply(RationalScalar.of(+1, 24));
    return ap2(ad, x, y).subtract(xyxy);
  }
}

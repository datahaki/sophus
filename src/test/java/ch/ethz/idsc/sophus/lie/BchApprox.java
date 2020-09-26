// code by jph
package ch.ethz.idsc.sophus.lie;

import java.io.Serializable;
import java.util.function.BinaryOperator;

import ch.ethz.idsc.tensor.RationalScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.red.Total;

/** Reference: Neeb */
public class BchApprox implements BinaryOperator<Tensor>, Serializable {
  public static final int DEGREE = 4;

  /** @param ad
   * @return */
  public static BinaryOperator<Tensor> of(Tensor ad) {
    return new BchApprox(JacobiIdentity.require(ad));
  }

  /***************************************************/
  private final Tensor ad;

  public BchApprox(Tensor ad) {
    this.ad = ad;
  }

  @Override
  public Tensor apply(Tensor x, Tensor y) {
    return new Inner(x, y).sum();
  }

  /* package */ Tensor series(Tensor x, Tensor y) {
    return new Inner(x, y).sum;
  }

  private class Inner {
    private final Tensor sum;

    Inner(Tensor x, Tensor y) {
      Tensor xxy = ad.dot(x).dot(ad.dot(x).dot(y)).multiply(RationalScalar.of(+1, 12));
      Tensor yyx = ad.dot(y).dot(ad.dot(y).dot(x)).multiply(RationalScalar.of(+1, 12));
      Tensor xyxy = ad.dot(x).dot(ad.dot(y).dot(ad.dot(x).dot(y))).multiply(RationalScalar.of(-1, 24));
      sum = Tensors.of( //
          x.add(y), //
          ad.dot(x).dot(y).multiply(RationalScalar.HALF), //
          xxy.add(yyx), //
          xyxy);
    }

    private Tensor sum() {
      return Total.of(sum);
    }
  }
}

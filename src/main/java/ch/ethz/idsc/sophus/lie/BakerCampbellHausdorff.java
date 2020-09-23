// code by jph
package ch.ethz.idsc.sophus.lie;

import ch.ethz.idsc.tensor.RationalScalar;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.TensorRuntimeException;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.Append;
import ch.ethz.idsc.tensor.alg.Array;
import ch.ethz.idsc.tensor.alg.NestList;
import ch.ethz.idsc.tensor.alg.UnitVector;
import ch.ethz.idsc.tensor.mat.IdentityMatrix;
import ch.ethz.idsc.tensor.red.Times;
import ch.ethz.idsc.tensor.red.Total;
import ch.ethz.idsc.tensor.sca.Factorial;
import ch.ethz.idsc.tensor.sca.Power;
import ch.ethz.idsc.tensor.sca.Sign;

/** Reference: Neeb */
public class BakerCampbellHausdorff {
  /** @param ad
   * @param x
   * @param y
   * @return */
  public static Tensor of(Tensor ad, Tensor x, Tensor y, int degree) {
    return x.add(new BakerCampbellHausdorff(ad.dot(x), ad.dot(y), degree).sum.dot(y));
  }

  public static Tensor ap(int degree, Tensor ad, Tensor x, Tensor y) {
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

  /***************************************************/
  private final int n;
  private final Tensor[] pwX;
  private final Tensor[] pwY;
  /** max number of applications of ad */
  private final int degree;
  private Tensor sum;

  private BakerCampbellHausdorff(Tensor adX, Tensor adY, int degree) {
    this.degree = degree;
    n = adX.length();
    pwX = NestList.of(adX::dot, IdentityMatrix.of(n), degree).stream().toArray(Tensor[]::new);
    pwY = NestList.of(adY::dot, IdentityMatrix.of(n), degree).stream().toArray(Tensor[]::new);
    sum = Array.zeros(n, n);
    recur(Tensors.empty(), Tensors.empty(), 0);
  }

  public void recur(Tensor p, Tensor q, int m) {
    final int k = p.length();
    if (k != q.length())
      throw TensorRuntimeException.of(p, q);
    p.add(q).stream().map(Scalar.class::cast).forEach(Sign::requirePositive);
    Scalar factor = Power.of(-1, k) //
        .divide(RealScalar.of(k + 1)) //
        .divide(Total.ofVector(q).add(RealScalar.ONE));
    // ---
    Tensor prd = IdentityMatrix.of(n);
    for (int index = 0; index < k; ++index) {
      prd = prd.dot(pwX[p.Get(index).number().intValue()]);
      prd = prd.dot(pwY[q.Get(index).number().intValue()]);
    }
    prd = prd.dot(pwX[m]);
    // ---
    Scalar fp = p.stream().map(Scalar.class::cast).map(Factorial.FUNCTION).reduce(Times::of).orElse(RealScalar.ONE);
    Scalar fq = q.stream().map(Scalar.class::cast).map(Factorial.FUNCTION).reduce(Times::of).orElse(RealScalar.ONE);
    Scalar fm = Factorial.of(m);
    prd = prd.multiply(factor.divide(Times.of(fp, fq, fm)));
    sum = sum.add(prd);
    // ---
    int sp = Total.ofVector(p).number().intValue();
    int sq = Total.ofVector(q).number().intValue();
    if (sp + sq + m < degree) {
      for (int index = 0; index < k; ++index)
        recur(p.add(UnitVector.of(k, index)), q, m);
      for (int index = 0; index < k; ++index)
        recur(p, q.add(UnitVector.of(k, index)), m);
      // increase k
      recur(Append.of(p, RealScalar.ZERO), Append.of(q, RealScalar.ONE), m);
      recur(Append.of(p, RealScalar.ONE), Append.of(q, RealScalar.ZERO), m);
      // increase m
      recur(p, q, m + 1);
    }
  }
}

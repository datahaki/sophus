// code by jph
// adapted from code by jph 2006
package ch.ethz.idsc.sophus.lie;

import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.TensorRuntimeException;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.Append;
import ch.ethz.idsc.tensor.alg.Join;
import ch.ethz.idsc.tensor.mat.IdentityMatrix;
import ch.ethz.idsc.tensor.mat.Tolerance;
import ch.ethz.idsc.tensor.red.Times;
import ch.ethz.idsc.tensor.red.Total;
import ch.ethz.idsc.tensor.sca.Factorial;
import ch.ethz.idsc.tensor.sca.Power;
import ch.ethz.idsc.tensor.sca.Sign;

/** Reference: Neeb
 * Hakenberg.de kernel.nb */
public class BakerCampbellHausdorff {
  /** @param ad
   * @param x
   * @param y
   * @param degree
   * @return */
  public static Tensor of(Tensor ad, Tensor x, Tensor y, int degree) {
    return new BakerCampbellHausdorff(ad, x, y, degree).sum;
  }

  /***************************************************/
  private static final Scalar _0 = RealScalar.ZERO;
  private static final Scalar _1 = RealScalar.ONE;
  private final Tensor adX;
  private final Tensor adY;
  /** max number of applications of ad */
  private final int degree;
  private Tensor sum;

  private BakerCampbellHausdorff(Tensor ad, Tensor x, Tensor y, int degree) {
    this.degree = degree;
    sum = x;
    adX = ad.dot(x);
    adY = ad.dot(y);
    Tensor pwX = IdentityMatrix.of(x.length());
    for (int m = 0; m <= degree; ++m) {
      recur(pwX.dot(y).divide(Factorial.of(m)), m + 1, Tensors.empty(), Tensors.empty(), true);
      pwX = adX.dot(pwX);
    }
  }

  private void recur(Tensor v, int d, Tensor p, Tensor q, boolean incrementQ) {
    final int k = p.length();
    if (k != q.length())
      throw TensorRuntimeException.of(p, q);
    p.add(q).stream().map(Scalar.class::cast).forEach(Sign::requirePositive);
    if (Tolerance.CHOP.allZero(v))
      return;
    // ---
    Scalar factor = Power.of(-1, k) //
        .divide(RealScalar.of(k + 1)) //
        .divide(Total.ofVector(q).add(_1));
    Scalar f = Times.pmul(Join.of(p, q).map(Factorial.FUNCTION)).Get();
    sum = sum.add(v.multiply(factor.divide(f)));
    if (d <= degree) {
      if (0 < k) {
        if (incrementQ) {
          Tensor cq = q.copy();
          cq.set(RealScalar.ONE::add, k - 1);
          recur(adY.dot(v), d + 1, p, cq, true);
        }
        {
          Tensor cp = p.copy();
          cp.set(RealScalar.ONE::add, k - 1);
          recur(adX.dot(v), d + 1, cp, q, false);
        }
      }
      recur(adY.dot(v), d + 1, Append.of(p, _0), Append.of(q, _1), true);
      recur(adX.dot(v), d + 1, Append.of(p, _1), Append.of(q, _0), false);
    }
  }
}

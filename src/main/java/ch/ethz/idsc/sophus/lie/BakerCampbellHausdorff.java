// code by jph
// adapted from code by jph 2006
package ch.ethz.idsc.sophus.lie;

import java.io.Serializable;
import java.util.function.BinaryOperator;

import ch.ethz.idsc.tensor.Integers;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.Append;
import ch.ethz.idsc.tensor.alg.Array;
import ch.ethz.idsc.tensor.alg.Join;
import ch.ethz.idsc.tensor.mat.IdentityMatrix;
import ch.ethz.idsc.tensor.mat.Tolerance;
import ch.ethz.idsc.tensor.red.Times;
import ch.ethz.idsc.tensor.red.Total;
import ch.ethz.idsc.tensor.sca.Chop;
import ch.ethz.idsc.tensor.sca.Factorial;

/** Log[Exp[-y] Exp[-x]] = -Log[Exp[x] Exp[y]]
 * 
 * Reference: Neeb
 * Hakenberg.de kernel.nb */
public class BakerCampbellHausdorff implements BinaryOperator<Tensor>, Serializable {
  private static final long serialVersionUID = -5427741907026433689L;
  private static final Scalar _0 = RealScalar.ZERO;
  private static final Scalar _1 = RealScalar.ONE;
  private static final int[] SIGN = { 1, -1 };

  /** @param ad tensor of rank 3 that satisfies the Jacobi identity
   * @param degree positive
   * @return */
  public static BinaryOperator<Tensor> of(Tensor ad, int degree) {
    return new BakerCampbellHausdorff( //
        JacobiIdentity.require(ad), //
        Integers.requirePositive(degree), //
        Tolerance.CHOP);
  }

  /***************************************************/
  private final Tensor ad;
  private final int degree;
  private final Chop chop;

  private BakerCampbellHausdorff(Tensor ad, int degree, Chop chop) {
    this.ad = ad;
    this.degree = degree;
    this.chop = chop;
  }

  @Override
  public Tensor apply(Tensor x, Tensor y) {
    return new Inner(x, y).sum();
  }

  /* package */ Tensor series(Tensor x, Tensor y) {
    return new Inner(x, y).sum;
  }

  private class Inner {
    private final Tensor adX;
    private final Tensor adY;
    private final Tensor sum;

    Inner(Tensor x, Tensor y) {
      sum = Array.zeros(degree, x.length());
      sum.set(x, 0);
      adX = ad.dot(x);
      adY = ad.dot(y);
      Tensor pwX = IdentityMatrix.of(x.length());
      for (int m = 0; m < degree; ++m) {
        recur(pwX.dot(y).divide(Factorial.of(m)), m + 1, Tensors.empty(), Tensors.empty(), 0, true);
        pwX = adX.dot(pwX);
      }
    }

    private void recur(Tensor v, int d, Tensor p, Tensor q, int total_q, boolean incrementQ) {
      final int k = p.length();
      if (chop.allZero(v))
        return;
      Scalar f = RealScalar.of(Math.multiplyExact(SIGN[k % 2] * (k + 1), total_q + 1)) //
          .multiply(Times.pmul(Join.of(p, q).map(Factorial.FUNCTION)).Get());
      sum.set(v.divide(f)::add, d - 1);
      if (d < degree) {
        if (0 < k) {
          if (incrementQ) {
            Tensor cq = q.copy();
            cq.set(RealScalar.ONE::add, k - 1);
            recur(adY.dot(v), d + 1, p, cq, total_q + 1, true);
          }
          {
            Tensor cp = p.copy();
            cp.set(RealScalar.ONE::add, k - 1);
            recur(adX.dot(v), d + 1, cp, q, total_q, false);
          }
        }
        recur(adY.dot(v), d + 1, Append.of(p, _0), Append.of(q, _1), total_q + 1, true);
        recur(adX.dot(v), d + 1, Append.of(p, _1), Append.of(q, _0), total_q, false);
      }
    }

    private Tensor sum() {
      return Total.of(sum);
    }
  }
}

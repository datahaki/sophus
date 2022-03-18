// code by jph
package ch.alpine.sophus.lie;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.d.DiscreteUniformDistribution;

/** References:
 * https://arxiv.org/abs/1604.05281
 * 
 * https://en.wikipedia.org/wiki/Jacobi_identity */
public enum HigherJacobiIdentity {
  ;
  public static void of4(Tensor ad) {
    int n = ad.length();
    Distribution distribution = DiscreteUniformDistribution.of(-5, 6);
    Tensor x = RandomVariate.of(distribution, n);
    Tensor y = RandomVariate.of(distribution, n);
    Tensor z = RandomVariate.of(distribution, n);
    Tensor w = RandomVariate.of(distribution, n);
    // ---
    Tensor t1 = ad.dot(x).dot(ad.dot(y).dot(ad.dot(z).dot(w)));
    Tensor t2 = ad.dot(y).dot(ad.dot(x).dot(ad.dot(w).dot(z)));
    Tensor t3 = ad.dot(z).dot(ad.dot(w).dot(ad.dot(x).dot(y)));
    Tensor t4 = ad.dot(w).dot(ad.dot(z).dot(ad.dot(y).dot(x)));
    Tensor sum = t1.add(t2).add(t3).add(t4);
    {
      NestedBracket nestedBracket = new NestedBracket(ad);
      Tolerance.CHOP.requireClose(t1, nestedBracket.r2l(x, y, z, w));
      Tolerance.CHOP.requireClose(t2, nestedBracket.r2l(y, x, w, z));
      Tolerance.CHOP.requireClose(t3, nestedBracket.r2l(z, w, x, y));
      Tolerance.CHOP.requireClose(t4, nestedBracket.r2l(w, z, y, x));
    }
    Tolerance.CHOP.requireAllZero(sum);
  }

  public static void of4b(Tensor ad) {
    int n = ad.length();
    Distribution distribution = DiscreteUniformDistribution.of(-5, 6);
    Tensor x1 = RandomVariate.of(distribution, n);
    Tensor x2 = RandomVariate.of(distribution, n);
    Tensor x3 = RandomVariate.of(distribution, n);
    Tensor x4 = RandomVariate.of(distribution, n);
    // ---
    NestedBracket nestedBracket = new NestedBracket(ad);
    Tensor t1 = nestedBracket.l2r(x1, x2, x3, x4);
    Tensor t2 = nestedBracket.l2r(x2, x1, x4, x3);
    Tensor t3 = nestedBracket.l2r(x3, x4, x1, x2);
    Tensor t4 = nestedBracket.l2r(x4, x3, x2, x1);
    Tensor sum = t1.add(t2).add(t3).add(t4);
    Tolerance.CHOP.requireAllZero(sum);
  }

  public static void of5(Tensor ad) {
    int n = ad.length();
    Distribution distribution = DiscreteUniformDistribution.of(-5, 6);
    Tensor x1 = RandomVariate.of(distribution, n);
    Tensor x2 = RandomVariate.of(distribution, n);
    Tensor x3 = RandomVariate.of(distribution, n);
    Tensor x4 = RandomVariate.of(distribution, n);
    Tensor x5 = RandomVariate.of(distribution, n);
    // ---
    NestedBracket nestedBracket = new NestedBracket(ad);
    Tensor t1 = nestedBracket.l2r(x1, x2, x3, x4, x5);
    Tensor t2 = nestedBracket.l2r(x2, x1, x4, x3, x5);
    Tensor t3 = nestedBracket.l2r(x2, x1, x5, x3, x4);
    Tensor t4 = nestedBracket.l2r(x1, x2, x5, x4, x3);
    Tensor t5 = nestedBracket.l2r(x3, x4, x5, x1, x2);
    Tensor t6 = nestedBracket.l2r(x4, x3, x5, x2, x1);
    Tensor sum = t1.add(t2).add(t3).add(t4).add(t5).add(t6);
    Tolerance.CHOP.requireAllZero(sum);
  }

  public static void of5b(Tensor ad) {
    int n = ad.length();
    Distribution distribution = DiscreteUniformDistribution.of(-5, 6);
    Tensor b = RandomVariate.of(distribution, n);
    Tensor a = RandomVariate.of(distribution, n);
    Tensor z = RandomVariate.of(distribution, n);
    Tensor y = RandomVariate.of(distribution, n);
    Tensor x = RandomVariate.of(distribution, n);
    // ---
    NestedBracket nestedBracket = new NestedBracket(ad);
    Tensor t1 = nestedBracket.r2l(x, y, z, a, b);
    Tensor t2 = nestedBracket.r2l(x, z, y, b, a);
    Tensor t3 = nestedBracket.r2l(y, z, x, b, a);
    Tensor t4 = nestedBracket.r2l(z, y, x, a, b);
    Tensor t5 = nestedBracket.r2l(a, b, x, y, z);
    Tensor t6 = nestedBracket.r2l(b, a, x, z, y);
    Tensor sum = t1.add(t2).add(t3).add(t4).add(t5).add(t6);
    Tolerance.CHOP.requireAllZero(sum);
  }
}

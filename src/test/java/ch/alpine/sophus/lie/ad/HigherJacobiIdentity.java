// code by jph
package ch.alpine.sophus.lie.ad;

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
  public static void of(Tensor ad) {
    int n = ad.length();
    Distribution distribution = DiscreteUniformDistribution.of(-5, 6);
    Tensor x = RandomVariate.of(distribution, n);
    Tensor y = RandomVariate.of(distribution, n);
    Tensor z = RandomVariate.of(distribution, n);
    Tensor w = RandomVariate.of(distribution, n);
    Tensor t1 = ad.dot(x).dot(ad.dot(y).dot(ad.dot(z).dot(w)));
    Tensor t2 = ad.dot(y).dot(ad.dot(x).dot(ad.dot(w).dot(z)));
    Tensor t3 = ad.dot(z).dot(ad.dot(w).dot(ad.dot(x).dot(y)));
    Tensor t4 = ad.dot(w).dot(ad.dot(z).dot(ad.dot(y).dot(x)));
    Tensor sum = t1.add(t2).add(t3).add(t4);
    Tolerance.CHOP.requireAllZero(sum);
  }
}

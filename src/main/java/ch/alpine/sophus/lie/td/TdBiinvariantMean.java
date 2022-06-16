// code by ob, jph
package ch.alpine.sophus.lie.td;

import ch.alpine.sophus.bm.BiinvariantMean;
import ch.alpine.sophus.lie.rn.RnBiinvariantMean;
import ch.alpine.sophus.lie.sc.ScBiinvariantMean;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.Unprotect;
import ch.alpine.tensor.alg.Drop;
import ch.alpine.tensor.nrm.NormalizeTotal;
import ch.alpine.tensor.red.Times;
import ch.alpine.tensor.sca.exp.Logc;

/** @param sequence of (lambda_i, t_i) points in ST(n) and weights non-negative and normalized
 * @return associated biinvariant mean which is the solution to the barycentric equation
 * 
 * Reference 1:
 * "Exponential Barycenters of the Canonical Cartan Connection and Invariant Means on Lie Groups"
 * by Xavier Pennec, Vincent Arsigny, p.26, Section 4.1, 2012:
 * "ST (n) is one of the most simple non-compact and non-commutative Lie groups. As expected for
 * such Lie groups, it has no bi-invariant metric."
 * 
 * Reference 2:
 * "Bi-invariant Means in Lie Groups. Application to Left-invariant Polyaffine Transformations."
 * by Vincent Arsigny, Xavier Pennec, Nicholas Ayache, p.29, 2006 */
/* package */ enum TdBiinvariantMean implements BiinvariantMean {
  INSTANCE;

  @Override // from BiinvariantMean
  public Tensor mean(Tensor sequence, Tensor weights) {
    int n = Unprotect.dimension1Hint(sequence) - 1;
    Tensor lambdas = sequence.get(Tensor.ALL, n);
    // Reference 1, p.27 "weighted geometric mean of scalings"
    Scalar scmean = ScBiinvariantMean.INSTANCE.mean(lambdas.map(Tensors::of), weights).Get(0);
    Tensor alphaw = NormalizeTotal.FUNCTION.apply(Times.of(weights, lambdas.divide(scmean).map(Logc.FUNCTION)));
    Tensor tn_seq = Tensor.of(sequence.stream().map(row -> Drop.tail(row, 1)));
    Tensor trmean = RnBiinvariantMean.INSTANCE.mean(tn_seq, alphaw);
    return trmean.append(scmean); // "scalings reweighted arithmetic mean of translations"
  }
}

// code by ob
package ch.ethz.idsc.sophus.lie;

import ch.ethz.idsc.sophus.math.AffineQ;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.opt.BinaryAverage;

/** Quote [2012 Pennec, Arsigny; p.20]:
 * Although bi-invariant metrics may fail to exist, the group geodesics always exists in a Lie group
 * and one can define a bi-invariant mean implicitly as an exponential barycenter, at least locally.
 * As will be shown in the sequel, this definition has all the desirable invariance properties, even
 * when bi-invariant metrics do not exist. Moreover, we can show the existence and uniqueness of the
 * bi-invariant mean provided the dispersion of the data is small enough.
 * 
 * <p>Quote [2012 Pennec, Arsigny; p.4]:
 * The intuition behind such a bi-invariant mean on matrix Lie groups was present in [2003 Woods]
 * along with a practical iterative algorithm to compute it. However, no precise definition nor proof
 * of convergence was provided. The barycentric definition of bi-invariant means on Lie groups based
 * on one-parameter subgroups was developed in the the PhD of Vincent Arsigny and in the research
 * report [2006 Arsigny, Pennec, Ayache].
 * 
 * <p>
 * 1) The bi-invariant mean is invariant under simultaneous reordering of the input points and weights.
 * 2) The bi-invariant mean of two points p, q in G coincides with the binary average.
 * 3) For left-, right- and inverse-invariance of bi-invariant means see [2012 Pennec, Arsigny; p. 21].
 * 
 * <p>Reference:
 * "Exponential Barycenters of the Canonical Cartan Connection and Invariant Means on Lie Groups"
 * by Xavier Pennec, Vincent Arsigny, 2012
 * 
 * @see BinaryAverage */
@FunctionalInterface
public interface BiinvariantMean {
  /** Hint: implementations treat both parameters as immutable.
   * Implementations are not required to check for affinity of the weight vector.
   * 
   * @param sequence of points in Lie group
   * @param weights vector typically affine, and non-negative
   * @return bi-invariant mean
   * @throws Exception if sequence and weights are empty
   * @see AffineQ */
  Tensor mean(Tensor sequence, Tensor weights);
}

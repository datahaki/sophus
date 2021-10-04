// code by ob
package ch.alpine.sophus.lie.he;

import ch.alpine.sophus.bm.BiinvariantMean;
import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.ext.PackageTestAccess;

/** @param sequence of (x, y, z) points in He(n) of shape ((x1, ..., xm), (y1, ..., ym), z)
 * @param normalized non negative weights
 * @return associated biinvariant mean which is the solution to the barycentric equation
 * 
 * Reference 1:
 * "Bi-invariant Means in Lie Groups. Application to Left-invariant Polyaffine Transformations."
 * Vincent Arsigny, Xavier Pennec, Nicholas Ayache, p. 32, 2006
 * 
 * Reference 2:
 * "Exponential Barycenters of the Canonical Cartan Connection and Invariant Means on Lie Groups"
 * by Xavier Pennec, Vincent Arsigny, p.29, Section 4.2, 2012 */
public enum HeBiinvariantMean implements BiinvariantMean {
  INSTANCE;

  @PackageTestAccess
  static Tensor xydot(Tensor sequence) {
    return Tensor.of(sequence.stream().map(xyz -> xyz.get(0).dot(xyz.get(1))));
  }

  @Override // from BiinvariantMean
  public Tensor mean(Tensor sequence, Tensor weights) {
    Tensor ws = weights.dot(sequence);
    Tensor xMean = ws.get(0);
    Tensor yMean = ws.get(1);
    Tensor xyMean = weights.dot(xydot(sequence));
    return Tensors.of( //
        xMean, //
        yMean, //
        ws.Get(2).add(xMean.dot(yMean).subtract(xyMean).multiply(RationalScalar.HALF)));
  }
}

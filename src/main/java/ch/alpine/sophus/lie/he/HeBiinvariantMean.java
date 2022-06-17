// code by ob
package ch.alpine.sophus.lie.he;

import ch.alpine.sophus.bm.BiinvariantMean;
import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.Tensor;
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
    return Tensor.of(sequence.stream() //
        .map(HeFormat::of) //
        .map(xyz -> xyz.x().dot(xyz.y())));
  }

  @Override // from BiinvariantMean
  public Tensor mean(Tensor sequence, Tensor weights) {
    HeFormat heFormat = HeFormat.of(weights.dot(sequence));
    Tensor xMean = heFormat.x();
    Tensor yMean = heFormat.y();
    Tensor xyMean = weights.dot(xydot(sequence));
    return heFormat.with( //
        heFormat.z().add(xMean.dot(yMean).subtract(xyMean).multiply(RationalScalar.HALF)));
  }
}

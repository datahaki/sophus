// code by jph
package ch.alpine.sophus.lie.he;

import ch.alpine.sophus.bm.BiinvariantMean;
import ch.alpine.sophus.hs.Exponential;
import ch.alpine.sophus.lie.AbstractLieGroup;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.VectorQ;
import ch.alpine.tensor.chq.MemberQ;
import ch.alpine.tensor.ext.Integers;

/** (2 * n + 1)-dimensional Heisenberg group
 * 
 * <p>the neutral element is {0, ..., 0, 0, ..., 0, 0}
 * 
 * <p>Reference 1:
 * Bi-invariant Means in Lie Groups. Application to Left-invariant Polyaffine Transformations.
 * by Vincent Arsigny, Xavier Pennec, Nicholas Ayache, pp 31-32, 2006
 * 
 * Reference 2:
 * "Exponential Barycenters of the Canonical Cartan Connection and Invariant Means on Lie Groups"
 * by Xavier Pennec, Vincent Arsigny, p.29, Section 4.2, 2012
 * 
 * @param sequence of (x, y, z) points in He(n) of shape ((x1, ..., xm), (y1, ..., ym), z)
 * @param normalized non-negative weights
 * @return associated biinvariant mean which is the solution to the barycentric equation
 * 
 * Reference 1:
 * "Bi-invariant Means in Lie Groups. Application to Left-invariant Polyaffine Transformations."
 * Vincent Arsigny, Xavier Pennec, Nicholas Ayache, p. 32, 2006
 * 
 * Reference 2:
 * "Exponential Barycenters of the Canonical Cartan Connection and Invariant Means on Lie Groups"
 * by Xavier Pennec, Vincent Arsigny, p.29, Section 4.2, 2012 */
public class HeGroup extends AbstractLieGroup {
  public static final HeGroup INSTANCE = new HeGroup();

  @Override
  public BiinvariantMean biinvariantMean() {
    return HeBiinvariantMean.INSTANCE;
  }

  @Override
  public MemberQ isPointQ() {
    return uvw -> VectorQ.of(uvw) && Integers.isOdd(uvw.length());
  }

  @Override
  public final Exponential exponential0() {
    return HeExponential.INSTANCE;
  }

  @Override
  public Tensor neutral(Tensor element) {
    return element.maps(Scalar::zero);
  }

  @Override
  public Tensor invert(Tensor element) {
    return HeFormat.of(element).inverse().toCoordinate();
  }

  @Override
  public Tensor combine(Tensor element1, Tensor element2) {
    return HeFormat.of(element1).combine(HeFormat.of(element2)).toCoordinate();
  }

  @Override // from LieGroupElement
  public Tensor adjoint(Tensor xyz, Tensor dxdydz) {
    HeFormat heFormat = HeFormat.of(xyz);
    HeFormat other = HeFormat.of(dxdydz);
    return other.with((Scalar) heFormat.x().dot(other.y()).subtract(heFormat.y().dot(other.x())).add(other.z()));
  }

  @Override // from LieGroupElement
  public Tensor dL(Tensor xyz, Tensor tensor) {
    throw new UnsupportedOperationException();
  }

  @Override
  public String toString() {
    return "He";
  }
}

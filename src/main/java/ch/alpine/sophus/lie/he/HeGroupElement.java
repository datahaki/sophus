// code by jph
package ch.alpine.sophus.lie.he;

import java.io.Serializable;

import ch.alpine.sophus.lie.LieGroupElement;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;

/** element of (2 * n + 1)-dimensional Heisenberg group
 * 
 * <p>the neutral element is {0, ..., 0, 0, ..., 0, 0}
 * 
 * <p>Reference 1:
 * Bi-invariant Means in Lie Groups. Application to Left-invariant Polyaffine Transformations.
 * by Vincent Arsigny, Xavier Pennec, Nicholas Ayache, pp 31-32, 2006
 * 
 * Reference 2:
 * "Exponential Barycenters of the Canonical Cartan Connection and Invariant Means on Lie Groups"
 * by Xavier Pennec, Vincent Arsigny, p.29, Section 4.2, 2012 */
public class HeGroupElement implements LieGroupElement, Serializable {
  private final HeFormat heFormat;

  /** @param xyz vector of the form {x1, ...., xn, y1, ..., yn, z} */
  public HeGroupElement(Tensor xyz) {
    this(HeFormat.of(xyz));
  }

  private HeGroupElement(HeFormat heFormat) {
    this.heFormat = heFormat;
  }

  @Override // from LieGroupElement
  public Tensor toCoordinate() {
    return heFormat.toCoordinate();
  }

  @Override // from LieGroupElement
  public HeGroupElement inverse() {
    return new HeGroupElement(heFormat.inverse());
  }

  @Override // from LieGroupElement
  public Tensor combine(Tensor xyz) {
    return heFormat.combine(HeFormat.of(xyz)).toCoordinate();
  }

  @Override // from LieGroupElement
  public Tensor adjoint(Tensor dxdydz) {
    HeFormat other = HeFormat.of(dxdydz);
    return other.with((Scalar) heFormat.x().dot(other.y()).subtract(heFormat.y().dot(other.x())).add(other.z()));
  }

  @Override // from LieGroupElement
  public Tensor dL(Tensor tensor) {
    throw new UnsupportedOperationException();
  }
}

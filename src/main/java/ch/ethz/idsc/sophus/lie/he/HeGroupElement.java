// code by jph
package ch.ethz.idsc.sophus.lie.he;

import java.io.Serializable;

import ch.ethz.idsc.sophus.lie.LieGroupElement;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.VectorQ;

/** element of (2 * n + 1)-dimensional Heisenberg group
 * 
 * <p>the neutral element is {{0, ..., 0}, {0, ..., 0}, 0}
 * 
 * <p>Reference 1:
 * Bi-invariant Means in Lie Groups. Application to Left-invariant Polyaffine Transformations.
 * by Vincent Arsigny, Xavier Pennec, Nicholas Ayache, pp 31-32, 2006
 * 
 * Reference 2:
 * "Exponential Barycenters of the Canonical Cartan Connection and Invariant Means on Lie Groups"
 * by Xavier Pennec, Vincent Arsigny, p.29, Section 4.2, 2012 */
public class HeGroupElement implements LieGroupElement, Serializable {
  private static final long serialVersionUID = 7733121807627532655L;
  private final Tensor x;
  private final Tensor y;
  private final Scalar z;

  /** @param xyz of the form {{x1, ...., xn}, {y1, ..., yn}, z} */
  public HeGroupElement(Tensor xyz) {
    this( //
        VectorQ.require(xyz.get(0)), //
        VectorQ.require(xyz.get(1)), //
        xyz.Get(2));
  }

  private HeGroupElement(Tensor x, Tensor y, Scalar z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }

  @Override // from LieGroupElement
  public Tensor toCoordinate() {
    return Tensors.of(x, y, z);
  }

  @Override // from LieGroupElement
  public HeGroupElement inverse() {
    return new HeGroupElement( //
        x.negate(), //
        y.negate(), //
        (Scalar) x.dot(y).subtract(z));
  }

  @Override // from LieGroupElement
  public Tensor combine(Tensor xyz) {
    HeGroupElement heGroupElement = new HeGroupElement(xyz);
    return Tensors.of( //
        x.add(heGroupElement.x), //
        y.add(heGroupElement.y), //
        z.add(heGroupElement.z).add(x.dot(heGroupElement.y)));
  }

  @Override // from LieGroupElement
  public Tensor adjoint(Tensor dxdydz) {
    Tensor dx = dxdydz.get(0);
    Tensor dy = dxdydz.get(1);
    return Tensors.of(dx, dy, x.dot(dy).subtract(y.dot(dx)).add(dxdydz.get(2)));
  }

  @Override // from LieGroupElement
  public Tensor dL(Tensor tensor) {
    throw new UnsupportedOperationException();
  }
}

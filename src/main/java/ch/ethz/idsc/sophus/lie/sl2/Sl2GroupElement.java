// code by jph
package ch.ethz.idsc.sophus.lie.sl2;

import ch.ethz.idsc.sophus.lie.LieGroupElement;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Scalars;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.TensorRuntimeException;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.VectorQ;

/** neutral element is {0, 0, 1}
 * 
 * Reference:
 * "Theorie der Transformationsgruppen - Erster Abschnitt"
 * Marius Sophus Lie, Teubner 1930
 * 
 * "On Lorentzian Ricci-Flat Homogeneous Manifolds"
 * by Jan Hakenberg, 2006 */
public class Sl2GroupElement implements LieGroupElement {
  public static Sl2GroupElement create(Tensor vector) {
    Scalar a3 = vector.Get(2);
    if (Scalars.isZero(a3))
      throw TensorRuntimeException.of(vector);
    Scalar a1 = vector.Get(0);
    Scalar a2 = vector.Get(1);
    return new Sl2GroupElement(a1, a2, a3);
  }

  /***************************************************/
  private final Scalar a1;
  private final Scalar a2;
  private final Scalar a3;

  /** @param a1
   * @param a2
   * @param a3 non-zero */
  private Sl2GroupElement(Scalar a1, Scalar a2, Scalar a3) {
    this.a1 = a1;
    this.a2 = a2;
    this.a3 = a3;
  }

  @Override // from LieGroupElement
  public Tensor toCoordinate() {
    return Tensors.of(a1, a2, a3);
  }

  @Override // from LieGroupElement
  public Sl2GroupElement inverse() {
    return new Sl2GroupElement( //
        a1.divide(a3).negate(), //
        a2.divide(a3).negate(), //
        a3.reciprocal());
  }

  @Override // from LieGroupElement
  public Tensor combine(Tensor tensor) {
    Scalar b1 = tensor.Get(0);
    Scalar b2 = tensor.Get(1);
    Scalar b3 = tensor.Get(2);
    Scalar den = RealScalar.ONE.add(b1.multiply(a2));
    return Tensors.of( //
        a1.add(b1.multiply(a3)).divide(den), //
        b2.add(a2.multiply(b3)).divide(den), //
        b2.multiply(a1).add(b3.multiply(a3)).divide(den));
  }

  public Tensor adjoint() {
    Scalar factor = a3.subtract(a1.multiply(a2));
    return Tensors.matrix(new Scalar[][] { //
        { a3.multiply(a3), a1.multiply(a1).negate(), a3.multiply(a1).negate() }, //
        { a2.multiply(a2).negate(), RealScalar.ONE, a2 }, //
        { a3.multiply(a2).multiply(RealScalar.of(-2)), a1.add(a1), a3.add(a1.multiply(a2)) } }).divide(factor);
  }

  @Override // from LieGroupElement
  public Tensor adjoint(Tensor tensor) {
    return adjoint().dot(VectorQ.require(tensor));
  }

  @Override // from LieGroupElement
  public Tensor dL(Tensor tensor) {
    throw new UnsupportedOperationException();
  }
}

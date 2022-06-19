// code by jph
package ch.alpine.sophus.lie;

import ch.alpine.sophus.math.api.GroupElement;
import ch.alpine.tensor.Tensor;

/** interface of an element of a Lie-group
 * 
 * Reference:
 * "Exponential Barycenters of the Canonical Cartan Connection and Invariant Means on Lie Groups"
 * by Pennec, Arsigny, 2012, p.13 */
public interface LieGroupElement extends GroupElement {
  @Override
  LieGroupElement inverse();

  /** the adjoint map is a linear map on the lie algebra with full rank
   * the adjoint map is the differential at the neutral element of the conjugation
   * c_g : G -> G
   * c_g(x) = g.x.g^-1
   * 
   * <pre>
   * g.Exp[x] == Exp[Ad(g).x].g
   * Log[g.m.g^-1] == Ad(g).Log[m]
   * </pre>
   * 
   * @param tensor element of the lie algebra
   * @return Ad(this).tensor */
  Tensor adjoint(Tensor tensor);

  /** dL maps a vector from the lie algebra to the tangent space at this element
   * 
   * DL_x|e : TeG -> TxG
   * DL_x|e . Log[x^-1 . p] == Log_x[p]
   * 
   * @param tensor
   * @return */
  Tensor dL(Tensor tensor);

  /** dR
   * 
   * @param tensor
   * @return */
  default Tensor dR(Tensor tensor) {
    throw new UnsupportedOperationException();
  }
}

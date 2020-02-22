// code by jph
package ch.ethz.idsc.sophus.lie;

import ch.ethz.idsc.tensor.Tensor;

/** interface of an element of a Lie-group
 * 
 * Reference:
 * "Exponential Barycenters of the Canonical Cartan Connection and Invariant Means on Lie Groups"
 * by Pennec, Arsigny, 2012, p.13 */
public interface LieGroupElement {
  /** @return unique coordinate that represents this lie group element */
  Tensor toCoordinate();

  /** @return inverse of this element */
  LieGroupElement inverse();

  /** @param tensor
   * @return group action of this element and the element defined by given tensor */
  Tensor combine(Tensor tensor);

  /** the adjoint map is a linear map on the lie algebra with full rank
   * 
   * <pre>
   * g.Exp[x] == Exp[Ad(g).x].g
   * Log[g.m.g^-1] == Ad(g).Log[m]
   * </pre>
   * 
   * @param tensor element of the lie algebra
   * @return Ad(this).tensor */
  Tensor adjoint(Tensor tensor);

  default Tensor dL(Tensor tensor) {
    throw new UnsupportedOperationException();
  }

  default Tensor dR(Tensor tensor) {
    throw new UnsupportedOperationException();
  }
}

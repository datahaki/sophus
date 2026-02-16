// code by jph
package ch.alpine.sophus.lie;

import ch.alpine.sophus.hs.HomogeneousSpace;
import ch.alpine.sophus.math.api.Exponential;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.GroupInterface;
import ch.alpine.tensor.api.TensorUnaryOperator;

/** interface maps tensor coordinate to an element of a lie group
 * 
 * exponential at neutral element */
public interface LieGroup extends HomogeneousSpace, GroupInterface<Tensor>, LieIntegrator {
  /** @return tangent space at neutral element which happens to be the lie algebra */
  Exponential exponential0();

  /** @param g
   * @return h -> g.h.g^-1 */
  TensorUnaryOperator conjugation(Tensor g);

  /** @param g
   * @return h -> g.h */
  TensorUnaryOperator actionL(Tensor g);

  /** @param g
   * @return h -> h.g */
  TensorUnaryOperator actionR(Tensor g);

  TensorUnaryOperator diffOp(Tensor p);

  /** Ad:G→GL(g)
   * 
   * the adjoint map is a linear map on the lie algebra with full rank
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
  Tensor adjoint(Tensor point, Tensor tensor);

  TensorUnaryOperator adjoint(Tensor p);

  TensorUnaryOperator inverse();

  /** dL maps a vector from the lie algebra to the tangent space at this element
   * 
   * DL_x|e : TeG -> TxG
   * DL_x|e . Log[x^-1 . p] == Log_x[p]
   * 
   * @param tensor
   * @return */
  Tensor dL(Tensor point, Tensor tensor);

  /** dR
   * 
   * @param tensor
   * @return */
  default Tensor dR(Tensor point, Tensor tensor) {
    throw new UnsupportedOperationException();
  }
}

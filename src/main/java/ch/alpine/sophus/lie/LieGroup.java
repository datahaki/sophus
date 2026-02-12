// code by jph
package ch.alpine.sophus.lie;

import java.io.Serializable;
import java.util.Objects;

import ch.alpine.sophus.hs.Exponential;
import ch.alpine.sophus.hs.HomogeneousSpace;
import ch.alpine.sophus.hs.HsTransport;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.GroupInterface;
import ch.alpine.tensor.api.TensorUnaryOperator;
import ch.alpine.tensor.chq.ZeroDefectArrayQ;

/** interface maps tensor coordinate to an element of a lie group
 * 
 * exponential at neutral element */
// TODO SOPHUS API should give LieAlgebra with ad (since this is implied from basis/group action)
// .. and vectorLog implies basis
public interface LieGroup extends HomogeneousSpace, GroupInterface<Tensor>, LieIntegrator {
  /** @return tangent space */
  Exponential exponential0();

  abstract class LieExp implements Exponential, Serializable {
    // ---
  }

  /** all tangent vectors are assumed to be in the tangent space at the neutral element,
   * i.e. given in the basis of TeG */
  @Override // from Manifold
  default Exponential exponential(Tensor p) {
    return new LieExp() {
      Tensor pinv;

      @Override // from Exponential
      public Tensor exp(Tensor v) {
        return combine(p, exponential0().exp(v));
      }

      @Override // from Exponential
      public Tensor log(Tensor q) {
        if (Objects.isNull(pinv))
          pinv = invert(p);
        return exponential0().log(combine(pinv, q));
      }

      @Override // from Exponential
      public ZeroDefectArrayQ isTangentQ() {
        return exponential0().isTangentQ();
      }
    };
  }

  /** for a Lie groups, parallel transport of tangent vectors
   * is assumed to be along exp geodesics.
   * 
   * In this setting, a tangent vector at the neutral element
   * uniquely defines a left invariant vector field everywhere.
   * 
   * We propose the convention (but not require) that tangent
   * vectors are stated in the basis of the tangent space at
   * the neutral element, that is the Lie algebra. */
  @Override // from HomogeneousSpace
  default HsTransport hsTransport() {
    return (_, _) -> Tensor::copy;
  }

  /** @param g
   * @return h -> g.h.g^-1 */
  default TensorUnaryOperator conjugation(Tensor g) {
    Tensor inv_g = invert(g);
    return h -> combine(combine(g, h), inv_g);
  }

  /** @param g
   * @return h -> g.h */
  default TensorUnaryOperator actionL(Tensor g) {
    return h -> combine(g, h);
  }

  /** @param g
   * @return h -> h.g */
  default TensorUnaryOperator actionR(Tensor g) {
    return h -> combine(h, g);
  }

  default TensorUnaryOperator diffOp(Tensor p) {
    Tensor pinv = invert(p);
    return q -> combine(pinv, q);
  }

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

  default TensorUnaryOperator adjoint(Tensor p) {
    return x -> adjoint(p, x);
  }

  default TensorUnaryOperator inverse() {
    return p -> invert(p);
  }

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

  @Override // from LieIntegrator
  default Tensor spin(Tensor g, Tensor x) {
    return exponential(g).exp(x);
  }
}

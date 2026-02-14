// code by jph
package ch.alpine.sophus.lie;

import java.io.Serializable;
import java.util.Objects;

import ch.alpine.sophus.hs.Exponential;
import ch.alpine.sophus.hs.HsTransport;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.TensorUnaryOperator;
import ch.alpine.tensor.chq.ZeroDefectArrayQ;

public abstract class AbstractLieGroup implements LieGroup, Serializable {
  abstract class LieExp implements Exponential, Serializable {
    // ---
  }

  /** all tangent vectors are assumed to be in the tangent space at the neutral element,
   * i.e. given in the basis of TeG */
  @Override // from Manifold
  public final Exponential exponential(Tensor p) {
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
  public final HsTransport hsTransport() {
    return (_, _) -> Tensor::copy;
  }

  /** @param g
   * @return h -> g.h.g^-1 */
  @Override
  public final TensorUnaryOperator conjugation(Tensor g) {
    Tensor inv_g = invert(g);
    return h -> combine(combine(g, h), inv_g);
  }

  /** @param g
   * @return h -> g.h */
  @Override
  public final TensorUnaryOperator actionL(Tensor g) {
    return h -> combine(g, h);
  }

  /** @param g
   * @return h -> h.g */
  @Override
  public final TensorUnaryOperator actionR(Tensor g) {
    return h -> combine(h, g);
  }

  @Override
  public final TensorUnaryOperator diffOp(Tensor p) {
    Tensor pinv = invert(p);
    return q -> combine(pinv, q);
  }

  @Override
  public final TensorUnaryOperator adjoint(Tensor p) {
    return x -> adjoint(p, x);
  }

  @Override
  public final TensorUnaryOperator inverse() {
    return this::invert;
  }

  @Override // from LieIntegrator
  public final Tensor spin(Tensor p, Tensor v) {
    return exponential(p).exp(v);
  }
}

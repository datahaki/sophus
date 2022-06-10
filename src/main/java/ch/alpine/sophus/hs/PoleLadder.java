// code by jph
package ch.alpine.sophus.hs;

import java.io.Serializable;
import java.util.Objects;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.TensorUnaryOperator;

/** the pole ladder is exact in symmetric spaces
 * 
 * References:
 * "Efficient Parallel Transport of Deformations in Time Series of Images:
 * from Schild’s to Pole Ladder"
 * by Marco Lorenzi, Xavier Pennec, 2013
 * 
 * "Numerical Accuracy of Ladder Schemes for Parallel Transport on Manifolds"
 * by Nicolas Guigui, Xavier Pennec, 2020 p.14
 * 
 * @see SchildLadder */
public record PoleLadder(HomogeneousSpace homogeneousSpace) implements HsTransport, Serializable {
  public PoleLadder {
    Objects.requireNonNull(homogeneousSpace);
  }

  @Override // from HsTransport
  public TensorUnaryOperator shift(Tensor p, Tensor q) {
    return new Rung(p, q);
  }

  private class Rung implements TensorUnaryOperator {
    private final Exponential exp_p;
    private final Exponential exp_q;
    private final Exponential exp_m;

    private Rung(Tensor p, Tensor q) {
      exp_p = homogeneousSpace.exponential(p);
      exp_q = homogeneousSpace.exponential(q);
      Tensor m = exp_p.midpoint(q);
      exp_m = homogeneousSpace.exponential(m);
    }

    @Override
    public Tensor apply(Tensor v) {
      return exp_q.log(exp_m.flip(exp_p.exp(v))).negate();
    }
  }
}

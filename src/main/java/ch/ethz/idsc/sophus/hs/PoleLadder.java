// code by jph
package ch.ethz.idsc.sophus.hs;

import java.io.Serializable;
import java.util.Objects;

import ch.ethz.idsc.sophus.hs.gr.GrTransport;
import ch.ethz.idsc.sophus.hs.hn.HnTransport;
import ch.ethz.idsc.sophus.math.Exponential;
import ch.ethz.idsc.sophus.math.MidpointInterface;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.api.TensorUnaryOperator;

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
 * @see SchildLadder
 * @see GrTransport
 * @see HnTransport */
public class PoleLadder implements HsTransport, Serializable {
  /** @param hsManifold
   * @param midpointInterface
   * @return */
  public static HsTransport of(HsManifold hsManifold, MidpointInterface midpointInterface) {
    return new PoleLadder( //
        Objects.requireNonNull(hsManifold), //
        Objects.requireNonNull(midpointInterface));
  }

  /** @param hsManifold
   * @return */
  public static HsTransport of(HsManifold hsManifold) {
    return new PoleLadder(Objects.requireNonNull(hsManifold), null);
  }

  /***************************************************/
  private final HsManifold hsManifold;
  private final MidpointInterface midpointInterface;

  private PoleLadder(HsManifold hsManifold, MidpointInterface midpointInterface) {
    this.hsManifold = hsManifold;
    this.midpointInterface = midpointInterface;
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
      exp_p = hsManifold.exponential(p);
      exp_q = hsManifold.exponential(q);
      Tensor m = Objects.isNull(midpointInterface) //
          ? HsMidpoint.of(exp_p, q)
          : midpointInterface.midpoint(p, q);
      exp_m = hsManifold.exponential(m);
    }

    @Override
    public Tensor apply(Tensor v) {
      // return exp_q.log(exp_m.exp(exp_m.log(exp_p.exp(v)).negate())).negate();
      return exp_q.log(exp_m.flip(exp_p.exp(v))).negate();
    }
  }
}

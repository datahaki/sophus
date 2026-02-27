// code by jph
package ch.alpine.sophus.hs;

import java.io.Serializable;

import ch.alpine.sophus.api.TangentSpace;
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
  @Override // from HsTransport
  public TensorUnaryOperator shift(Tensor p, Tensor q) {
    TangentSpace exp_p = homogeneousSpace.tangentSpace(p);
    TangentSpace exp_q = homogeneousSpace.tangentSpace(q);
    Tensor m = homogeneousSpace.midpoint(p, q);
    return v -> exp_q.log(homogeneousSpace.flip(m, exp_p.exp(v))).negate();
  }
}

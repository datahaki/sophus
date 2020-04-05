// code by jph
package ch.ethz.idsc.sophus.crv.clothoid;

import java.io.Serializable;

import ch.ethz.idsc.tensor.Scalar;

/** Extension of clothoids in SE(2) to clothoids in SE(2) Covering
 * that respects winding numbers */
public class Se2CoveringClothoids extends Clothoids implements Serializable {
  public static final Clothoids INSTANCE = new Se2CoveringClothoids();

  private Se2CoveringClothoids() {
    // ---
  }

  @Override // from Clothoids
  public LagrangeQuadratic lagrangeQuadratic(Scalar b0, Scalar b1) {
    // TODO not final implementation
    return LagrangeQuadratic.interp(b0, MidpointTangentApproximation.LOCAL.apply(b0, b1), b1);
  }
}

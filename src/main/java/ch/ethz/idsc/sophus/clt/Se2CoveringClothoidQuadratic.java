// code by jph
package ch.ethz.idsc.sophus.clt;

import java.io.Serializable;

import ch.ethz.idsc.sophus.clt.mid.MidpointTangentApproximation;
import ch.ethz.idsc.tensor.Scalar;

/** Extension of clothoids in SE(2) to clothoids in SE(2) Covering
 * that respects winding numbers */
public enum Se2CoveringClothoidQuadratic implements ClothoidQuadratic, Serializable {
  INSTANCE;

  @Override // from Clothoids
  public LagrangeQuadratic lagrangeQuadratic(Scalar b0, Scalar b1) {
    // TODO not final implementation
    return LagrangeQuadratic.interp(b0, MidpointTangentApproximation.LAYERS.apply(b0, b1), b1);
  }
}

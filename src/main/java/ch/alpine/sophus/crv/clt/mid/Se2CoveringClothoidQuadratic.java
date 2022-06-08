// code by jph
package ch.alpine.sophus.crv.clt.mid;

import ch.alpine.sophus.crv.clt.LagrangeQuadratic;
import ch.alpine.tensor.Scalar;

/** Extension of clothoids in SE(2) to clothoids in SE(2) Covering
 * that respects winding numbers */
public enum Se2CoveringClothoidQuadratic implements ClothoidQuadratic {
  INSTANCE;

  @Override // from Clothoids
  public LagrangeQuadratic lagrangeQuadratic(Scalar b0, Scalar b1) {
    // TODO SOPHUS ALG not final implementation
    return LagrangeQuadratic.interp(b0, MidpointTangentApproximation.LAYERS.apply(b0, b1), b1);
  }
}

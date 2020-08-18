// code by jph
package ch.ethz.idsc.sophus.clt;

import java.io.Serializable;

import ch.ethz.idsc.sophus.clt.mid.MidpointTangentApproximation;
import ch.ethz.idsc.sophus.lie.so2.So2;
import ch.ethz.idsc.tensor.Scalar;

/** reif's solution */
public class Se2ClothoidBuilder extends AbstractClothoidBuilder implements Serializable {
  public static final ClothoidBuilder INSTANCE = new Se2ClothoidBuilder();

  private Se2ClothoidBuilder() {
    // ---
  }

  @Override // from Clothoids
  public LagrangeQuadratic lagrangeQuadratic(Scalar b0, Scalar b1) {
    b0 = So2.MOD.apply(b0); // normal form T0 == b0
    b1 = So2.MOD.apply(b1); // normal form T1 == b1
    // ---
    return LagrangeQuadratic.interp(b0, MidpointTangentApproximation.ORDER4.apply(b0, b1), b1);
  }
}

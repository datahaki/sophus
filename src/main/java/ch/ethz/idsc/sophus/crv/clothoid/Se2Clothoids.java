// code by jph
package ch.ethz.idsc.sophus.crv.clothoid;

import java.io.Serializable;

import ch.ethz.idsc.sophus.lie.so2.So2;
import ch.ethz.idsc.tensor.Scalar;

/** reif's solution */
public class Se2Clothoids extends Clothoids implements Serializable {
  public static final ClothoidInterface INSTANCE = new Se2Clothoids();

  private Se2Clothoids() {
    // ---
  }

  @Override // from Clothoids
  public LagrangeQuadratic lagrangeQuadratic(Scalar b0, Scalar b1) {
    b0 = So2.MOD.apply(b0); // normal form T0 == b0
    b1 = So2.MOD.apply(b1); // normal form T1 == b1
    // ---
    return LagrangeQuadratic.interp(b0, MidpointTangentApproximation.LOCAL.apply(b0, b1), b1);
  }
}

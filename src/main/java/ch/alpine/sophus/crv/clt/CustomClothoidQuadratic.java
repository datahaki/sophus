// code by jph
package ch.alpine.sophus.crv.clt;

import java.io.Serializable;

import ch.alpine.sophus.crv.clt.mid.ClothoidQuadratic;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.api.ScalarBinaryOperator;

/* package */ class CustomClothoidQuadratic implements ClothoidQuadratic, Serializable {
  private static final Scalar HALF = RealScalar.of(0.5);

  public static ClothoidQuadratic of(Scalar lambda) {
    return new CustomClothoidQuadratic((s1, s2) -> lambda);
  }

  // ---
  private final ScalarBinaryOperator binaryOperator;

  /** @param binaryOperator mapping (s1, s2) -> lambda */
  public CustomClothoidQuadratic(ScalarBinaryOperator binaryOperator) {
    this.binaryOperator = binaryOperator;
  }

  @Override // from ClothoidQuadratic
  public LagrangeQuadratic lagrangeQuadratic(Scalar b0, Scalar b1) {
    Scalar s1 = b0.add(b1).multiply(HALF);
    Scalar s2 = b0.subtract(b1).multiply(HALF);
    Scalar lambda = binaryOperator.apply(s1, s2);
    Scalar bm = lambda.add(s1);
    return LagrangeQuadratic.interp(b0, bm, b1);
  }
}

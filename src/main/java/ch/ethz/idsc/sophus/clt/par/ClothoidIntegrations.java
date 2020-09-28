// code by jph
package ch.ethz.idsc.sophus.clt.par;

import java.util.function.Function;

import ch.ethz.idsc.sophus.clt.LagrangeQuadratic;

public enum ClothoidIntegrations implements ClothoidIntegration {
  /** slower but more precise */
  ANALYTIC(AnalyticClothoidIntegral::new), //
  LEGENDRE(Legendre3ClothoidIntegral::new), //
  ;

  private final Function<LagrangeQuadratic, ClothoidIntegral> function;

  private ClothoidIntegrations(Function<LagrangeQuadratic, ClothoidIntegral> function) {
    this.function = function;
  }

  @Override // from ClothoidIntegration
  public ClothoidIntegral clothoidIntegral(LagrangeQuadratic lagrangeQuadratic) {
    return function.apply(lagrangeQuadratic);
  }
}

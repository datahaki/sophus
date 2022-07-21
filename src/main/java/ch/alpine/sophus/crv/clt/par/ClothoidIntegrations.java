// code by jph
package ch.alpine.sophus.crv.clt.par;

import java.util.function.Function;

import ch.alpine.sophus.crv.clt.LagrangeQuadratic;

public enum ClothoidIntegrations implements ClothoidIntegration {
  /** slower but more precise */
  ANALYTIC(AnalyticClothoidIntegral::new), //
  LEGENDRE(LegendreClothoidIntegral::new), //
  ;

  private final Function<LagrangeQuadratic, ClothoidIntegral> function;

  ClothoidIntegrations(Function<LagrangeQuadratic, ClothoidIntegral> function) {
    this.function = function;
  }

  @Override // from ClothoidIntegration
  public ClothoidIntegral clothoidIntegral(LagrangeQuadratic lagrangeQuadratic) {
    return function.apply(lagrangeQuadratic);
  }
}

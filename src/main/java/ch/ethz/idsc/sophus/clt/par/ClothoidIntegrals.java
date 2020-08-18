// code by jph
package ch.ethz.idsc.sophus.clt.par;

import java.util.function.Function;

import ch.ethz.idsc.sophus.clt.LagrangeQuadratic;

public enum ClothoidIntegrals {
  ANALYTIC(AnalyticClothoidIntegral::interp), //
  LEGENDRE(Legendre3ClothoidIntegral::new), //
  ;

  private final Function<LagrangeQuadratic, ClothoidIntegral> function;

  private ClothoidIntegrals(Function<LagrangeQuadratic, ClothoidIntegral> function) {
    this.function = function;
  }

  public ClothoidIntegral supply(LagrangeQuadratic lagrangeQuadratic) {
    return function.apply(lagrangeQuadratic);
  }
}

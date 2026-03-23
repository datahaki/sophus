// code by jph
package ch.alpine.sophus.clt;

public enum ClothoidIntegrations implements ClothoidIntegration {
  /** slower but more precise */
  ANALYTIC {
    @Override
    public ClothoidIntegral clothoidIntegral(LagrangeQuadratic lagrangeQuadratic) {
      return new ClothoidIntegralAnalytic(lagrangeQuadratic);
    }
  },
  LEGENDRE {
    @Override
    public ClothoidIntegral clothoidIntegral(LagrangeQuadratic lagrangeQuadratic) {
      return new ClothoidIntegralLegendre(lagrangeQuadratic);
    }
  };
}

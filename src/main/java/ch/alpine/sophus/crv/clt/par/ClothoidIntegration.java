// code by jph
package ch.alpine.sophus.crv.clt.par;

import ch.alpine.sophus.crv.clt.LagrangeQuadratic;

@FunctionalInterface
public interface ClothoidIntegration {
  /** @param lagrangeQuadratic
   * @return */
  ClothoidIntegral clothoidIntegral(LagrangeQuadratic lagrangeQuadratic);
}

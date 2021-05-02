// code by jph
package ch.alpine.sophus.clt.par;

import ch.alpine.sophus.clt.LagrangeQuadratic;

@FunctionalInterface
public interface ClothoidIntegration {
  /** @param lagrangeQuadratic
   * @return */
  ClothoidIntegral clothoidIntegral(LagrangeQuadratic lagrangeQuadratic);
}

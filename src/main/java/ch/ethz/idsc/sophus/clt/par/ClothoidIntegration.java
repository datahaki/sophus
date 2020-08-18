// code by jph
package ch.ethz.idsc.sophus.clt.par;

import ch.ethz.idsc.sophus.clt.LagrangeQuadratic;

@FunctionalInterface
public interface ClothoidIntegration {
  /** @param lagrangeQuadratic
   * @return */
  ClothoidIntegral clothoidIntegral(LagrangeQuadratic lagrangeQuadratic);
}

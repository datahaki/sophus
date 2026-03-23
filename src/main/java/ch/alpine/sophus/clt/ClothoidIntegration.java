// code by jph
package ch.alpine.sophus.clt;

@FunctionalInterface
public interface ClothoidIntegration {
  /** @param lagrangeQuadratic
   * @return */
  ClothoidIntegral clothoidIntegral(LagrangeQuadratic lagrangeQuadratic);
}

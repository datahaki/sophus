// code by jph
package ch.alpine.sophus.clt;

import ch.alpine.sophus.clt.mid.ClothoidQuadratic;
import ch.alpine.sophus.clt.mid.Se2ClothoidQuadratic;
import ch.alpine.sophus.clt.mid.Se2CoveringClothoidQuadratic;
import ch.alpine.sophus.clt.par.ClothoidIntegration;
import ch.alpine.sophus.clt.par.ClothoidIntegrations;

/** Extension of clothoids in SE(2) to clothoids in SE(2) Covering
 * that respects winding numbers */
public enum ClothoidBuilders {
  /** reif's solution */
  SE2_ANALYTIC(Se2ClothoidQuadratic.INSTANCE, ClothoidIntegrations.ANALYTIC), //
  /** reif's solution with lagrange quadrature: length is imprecise resulting in less precise curvature */
  SE2_LEGENDRE(Se2ClothoidQuadratic.INSTANCE, ClothoidIntegrations.LEGENDRE), //
  /** with windings */
  SE2_COVERING(Se2CoveringClothoidQuadratic.INSTANCE, ClothoidIntegrations.ANALYTIC), //
  ;

  private final ClothoidBuilder clothoidBuilder;

  private ClothoidBuilders(ClothoidQuadratic clothoidQuadratic, ClothoidIntegration clothoidIntegration) {
    this.clothoidBuilder = new ClothoidBuilderImpl(clothoidQuadratic, clothoidIntegration);
  }

  public ClothoidBuilder clothoidBuilder() {
    return clothoidBuilder;
  }
}

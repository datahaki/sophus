// code by jph
package ch.alpine.sophus.crv.clt;

import ch.alpine.sophus.crv.clt.mid.ClothoidQuadratic;
import ch.alpine.sophus.crv.clt.mid.Se2ClothoidQuadratic;
import ch.alpine.sophus.crv.clt.mid.Se2CoveringClothoidQuadratic;
import ch.alpine.sophus.crv.clt.par.ClothoidIntegration;
import ch.alpine.sophus.crv.clt.par.ClothoidIntegrations;

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

  ClothoidBuilders(ClothoidQuadratic clothoidQuadratic, ClothoidIntegration clothoidIntegration) {
    this.clothoidBuilder = new ClothoidBuilderImpl(clothoidQuadratic, clothoidIntegration);
  }

  public ClothoidBuilder clothoidBuilder() {
    return clothoidBuilder;
  }
}

// code by jph
package ch.ethz.idsc.sophus.clt;

import java.util.Arrays;
import java.util.List;

import ch.ethz.idsc.sophus.clt.mid.Se2ClothoidQuadratic;
import ch.ethz.idsc.sophus.clt.mid.Se2CoveringClothoidQuadratic;
import ch.ethz.idsc.sophus.clt.par.ClothoidIntegrations;

/** Extension of clothoids in SE(2) to clothoids in SE(2) Covering
 * that respects winding numbers */
public enum ClothoidBuilders {
  ;
  /** reif's solution */
  public static final ClothoidBuilder SE2_ANALYTIC = //
      new ClothoidBuilderImpl(Se2ClothoidQuadratic.INSTANCE, ClothoidIntegrations.ANALYTIC);
  /** reif's solution with lagrange quadrature: length is imprecise resulting in less precise curvature */
  public static final ClothoidBuilder SE2_LEGENDRE = //
      new ClothoidBuilderImpl(Se2ClothoidQuadratic.INSTANCE, ClothoidIntegrations.LEGENDRE);
  /** with windings */
  public static final ClothoidBuilder SE2_COVERING = //
      new ClothoidBuilderImpl(Se2CoveringClothoidQuadratic.INSTANCE, ClothoidIntegrations.ANALYTIC);
  // ---
  public static final List<ClothoidBuilder> VALUES = Arrays.asList( //
      SE2_ANALYTIC, //
      SE2_LEGENDRE, //
      SE2_COVERING);
}

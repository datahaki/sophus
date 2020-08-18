// code by jph
package ch.ethz.idsc.sophus.clt;

/** Extension of clothoids in SE(2) to clothoids in SE(2) Covering
 * that respects winding numbers */
public enum ClothoidBuilders {
  ;
  /** reif's solution */
  public static final ClothoidBuilder SE2 = //
      new ClothoidBuilderImpl(Se2ClothoidQuadratic.INSTANCE);
  /** with windings */
  public static final ClothoidBuilder SE2_COVERING = //
      new ClothoidBuilderImpl(Se2CoveringClothoidQuadratic.INSTANCE);
}

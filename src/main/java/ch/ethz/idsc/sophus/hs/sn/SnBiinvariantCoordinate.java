// code by jph
package ch.ethz.idsc.sophus.hs.sn;

import ch.ethz.idsc.sophus.hs.HsBiinvariantCoordinate;
import ch.ethz.idsc.sophus.hs.ProjectedCoordinate;
import ch.ethz.idsc.sophus.lie.rn.RnNorm;
import ch.ethz.idsc.sophus.lie.rn.RnNormSquared;
import ch.ethz.idsc.sophus.math.win.InverseNorm;

public enum SnBiinvariantCoordinate {
  ;
  public static final ProjectedCoordinate INSTANCE = //
      new HsBiinvariantCoordinate(SnManifold.INSTANCE, InverseNorm.of(RnNorm.INSTANCE));
  public static final ProjectedCoordinate SQUARED = //
      new HsBiinvariantCoordinate(SnManifold.INSTANCE, InverseNorm.of(RnNormSquared.INSTANCE));
}

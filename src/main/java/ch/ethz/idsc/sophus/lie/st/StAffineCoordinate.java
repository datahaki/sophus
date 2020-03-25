// code by jph
package ch.ethz.idsc.sophus.lie.st;

import ch.ethz.idsc.sophus.hs.HsBarycentricCoordinate;
import ch.ethz.idsc.sophus.hs.ProjectedCoordinate;
import ch.ethz.idsc.sophus.lie.LieFlattenLogManifold;

public enum StAffineCoordinate {
  ;
  public static final ProjectedCoordinate INSTANCE = HsBarycentricCoordinate.affine( //
      LieFlattenLogManifold.of(StGroup.INSTANCE, StExponential.INSTANCE::flattenLog));
}

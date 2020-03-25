// code by jph
package ch.ethz.idsc.sophus.lie.he;

import ch.ethz.idsc.sophus.hs.HsBarycentricCoordinate;
import ch.ethz.idsc.sophus.hs.ProjectedCoordinate;
import ch.ethz.idsc.sophus.lie.LieFlattenLogManifold;

public enum HeAffineCoordinate {
  ;
  public static final ProjectedCoordinate INSTANCE = //
      HsBarycentricCoordinate.affine(LieFlattenLogManifold.of(HeGroup.INSTANCE, HeExponential.INSTANCE::flattenLog));
}

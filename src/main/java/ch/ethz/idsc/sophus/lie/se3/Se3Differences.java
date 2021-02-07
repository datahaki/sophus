// code by jph
package ch.ethz.idsc.sophus.lie.se3;

import ch.ethz.idsc.sophus.lie.LieDifferences;

public enum Se3Differences {
  ;
  public static final LieDifferences INSTANCE = new LieDifferences(Se3Manifold.HS_EXP);
}

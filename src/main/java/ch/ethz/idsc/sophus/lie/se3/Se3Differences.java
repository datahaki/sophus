// code by jph
package ch.ethz.idsc.sophus.lie.se3;

import ch.ethz.idsc.sophus.hs.HsDifferences;

public enum Se3Differences {
  ;
  public static final HsDifferences INSTANCE = new HsDifferences(Se3Manifold.HS_EXP);
}

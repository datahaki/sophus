// code by jph
package ch.alpine.sophus.lie.se3;

import ch.alpine.sophus.lie.LieDifferences;

public enum Se3Differences {
  ;
  public static final LieDifferences INSTANCE = new LieDifferences(Se3Manifold.INSTANCE);
}

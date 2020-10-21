// code by jph
package ch.ethz.idsc.sophus.lie.r2;

import ch.ethz.idsc.sophus.gbc.Genesis;
import ch.ethz.idsc.sophus.gbc.MetricCoordinate;

public enum IterativeAffineCoordinate {
  ;
  /** @param k non-negative
   * @return iterative coordinates based on inverse distance coordinates */
  public static Genesis of(int k) {
    return new IterativeCoordinate(MetricCoordinate.affine(), k);
  }
}

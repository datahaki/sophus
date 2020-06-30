// code by jph
package ch.ethz.idsc.sophus.hs.r2;

import ch.ethz.idsc.sophus.lie.se2.Se2Group;
import ch.ethz.idsc.sophus.math.TensorMetric;

/** Careful: Se2Parametric is <em>not<em> a metric!
 * 
 * length of geodesic between p and q in SE(2) when projected to R^2
 * the projection is a circle segment
 * 
 * @see Se2CoveringParametric */
public class Se2Parametric extends Se2CoveringParametric {
  public static final TensorMetric INSTANCE = new Se2Parametric();

  /***************************************************/
  private Se2Parametric() {
    super(Se2Group.INSTANCE);
  }
}
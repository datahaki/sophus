// code by jph
package ch.alpine.sophus.hs;

import ch.alpine.sophus.api.TensorMetric;

public interface MetricSpace {
  /** @return distance operator that gives distance between two points p and q
   * that is invariant under all symmetries, or null if operator does not exist */
  TensorMetric biinvariantMetric();
}

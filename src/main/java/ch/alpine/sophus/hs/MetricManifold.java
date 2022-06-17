// code by jph
package ch.alpine.sophus.hs;

import ch.alpine.sophus.api.TensorMetric;

public interface MetricManifold extends TensorMetric {
  Biinvariant biinvariant();
}

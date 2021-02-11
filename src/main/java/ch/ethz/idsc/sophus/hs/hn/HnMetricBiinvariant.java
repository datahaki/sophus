// code by jph
package ch.ethz.idsc.sophus.hs.hn;

import ch.ethz.idsc.sophus.hs.Biinvariant;
import ch.ethz.idsc.sophus.hs.MetricBiinvariant;

public enum HnMetricBiinvariant {
  ;
  public static final Biinvariant INSTANCE = MetricBiinvariant.of(HnNorm.INSTANCE);
}

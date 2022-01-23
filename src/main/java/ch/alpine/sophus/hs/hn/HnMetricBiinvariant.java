// code by jph
package ch.alpine.sophus.hs.hn;

import ch.alpine.sophus.hs.Biinvariant;
import ch.alpine.sophus.hs.MetricBiinvariant;

public enum HnMetricBiinvariant {
  ;
  public static final Biinvariant INSTANCE = new MetricBiinvariant(HnVectorNorm::of);
}

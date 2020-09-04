// code by jph
package ch.ethz.idsc.sophus.math;

import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.red.Max;
import ch.ethz.idsc.tensor.red.Min;
import ch.ethz.idsc.tensor.red.ScalarSummaryStatistics;
import ch.ethz.idsc.tensor.sca.Clip;
import ch.ethz.idsc.tensor.sca.Clips;

public enum ClipCover {
  ;
  /** @param scalarSummaryStatistics
   * @return */
  public static Clip of(ScalarSummaryStatistics scalarSummaryStatistics) {
    return 0 < scalarSummaryStatistics.getCount() //
        ? Clips.interval(scalarSummaryStatistics.getMin(), scalarSummaryStatistics.getMax())
        : null;
  }

  /** @param clip
   * @param scalar
   * @return */
  public static Clip of(Clip clip, Scalar scalar) {
    return Clips.interval( //
        Min.of(clip.min(), scalar), //
        Max.of(clip.max(), scalar));
  }
}

// code by jph
package ch.ethz.idsc.sophus.hs.spd;

import ch.ethz.idsc.sophus.hs.HsInverseDistanceCoordinate;
import ch.ethz.idsc.sophus.lie.FlattenLog;
import ch.ethz.idsc.sophus.math.win.BarycentricCoordinate;
import ch.ethz.idsc.sophus.math.win.InverseDistanceWeighting;
import ch.ethz.idsc.tensor.Tensor;

public class SpdInverseDistanceCoordinate extends HsInverseDistanceCoordinate {
  public static final BarycentricCoordinate INSTANCE = //
      new SpdInverseDistanceCoordinate(InverseDistanceWeighting.of(SpdMetric.INSTANCE));
  public static final BarycentricCoordinate SQUARED = //
      new SpdInverseDistanceCoordinate(InverseDistanceWeighting.of(SpdMetricSquared.INSTANCE));

  public SpdInverseDistanceCoordinate(BarycentricCoordinate target) {
    super(target);
  }

  @Override
  public FlattenLog logAt(Tensor point) {
    return new SpdExp(point);
  }
}

// code by jph
package ch.ethz.idsc.sophus.hs.spd;

import ch.ethz.idsc.sophus.hs.HsBarycentricCoordinate;
import ch.ethz.idsc.sophus.lie.FlattenLog;
import ch.ethz.idsc.sophus.math.win.BarycentricCoordinate;
import ch.ethz.idsc.sophus.math.win.InverseDistanceWeighting;
import ch.ethz.idsc.tensor.Tensor;

public class SpdInverseDistanceCoordinate extends HsBarycentricCoordinate {
  public static final BarycentricCoordinate INSTANCE = //
      new SpdInverseDistanceCoordinate(InverseDistanceWeighting.of(SpdMetric.INSTANCE));

  private SpdInverseDistanceCoordinate(BarycentricCoordinate barycentricCoordinate) {
    super(barycentricCoordinate);
  }

  @Override
  public FlattenLog logAt(Tensor point) {
    return new SpdExp(point);
  }
}

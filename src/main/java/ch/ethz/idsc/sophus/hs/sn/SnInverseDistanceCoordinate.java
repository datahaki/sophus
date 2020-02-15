// code by jph
package ch.ethz.idsc.sophus.hs.sn;

import ch.ethz.idsc.sophus.math.NormalizeTotal;
import ch.ethz.idsc.sophus.math.win.BarycentricCoordinate;
import ch.ethz.idsc.sophus.math.win.InverseDistanceWeighting;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.mat.LeftNullSpace;
import ch.ethz.idsc.tensor.mat.PseudoInverse;

public class SnInverseDistanceCoordinate implements BarycentricCoordinate {
  public static final BarycentricCoordinate INSTANCE = //
      new SnInverseDistanceCoordinate(InverseDistanceWeighting.of(SnMetric.INSTANCE));
  public static final BarycentricCoordinate SQUARED = //
      new SnInverseDistanceCoordinate(InverseDistanceWeighting.of(SnMetricSquared.INSTANCE));
  /***************************************************/
  private final BarycentricCoordinate barycentricCoordinate;

  private SnInverseDistanceCoordinate(BarycentricCoordinate barycentricCoordinate) {
    this.barycentricCoordinate = barycentricCoordinate;
  }

  @Override // from BarycentricCoordinate
  public Tensor weights(Tensor sequence, Tensor mean) {
    Tensor target = barycentricCoordinate.weights(sequence, mean);
    Tensor levers = Tensor.of(sequence.stream().map(new SnExp(mean)::log));
    Tensor nullSpace = LeftNullSpace.of(levers);
    return NormalizeTotal.FUNCTION.apply(target.dot(PseudoInverse.of(nullSpace)).dot(nullSpace));
  }
}

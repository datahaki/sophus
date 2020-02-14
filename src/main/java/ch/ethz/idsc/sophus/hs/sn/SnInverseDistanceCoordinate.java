// code by jph
package ch.ethz.idsc.sophus.hs.sn;

import ch.ethz.idsc.sophus.math.NormalizeTotal;
import ch.ethz.idsc.sophus.math.win.BarycentricCoordinate;
import ch.ethz.idsc.sophus.math.win.InverseDistanceWeighting;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.mat.LeftNullSpace;
import ch.ethz.idsc.tensor.mat.PseudoInverse;

public enum SnInverseDistanceCoordinate implements BarycentricCoordinate {
  INSTANCE;

  private static final BarycentricCoordinate INVERSE_DISTANCE_WEIGHTING = //
      InverseDistanceWeighting.of(SnMetric.INSTANCE);

  @Override // from InverseDistanceCoordinates
  public Tensor weights(Tensor sequence, Tensor point) {
    Tensor target = INVERSE_DISTANCE_WEIGHTING.weights(sequence, point);
    Tensor levers = Tensor.of(sequence.stream().map(new SnExp(point)::log));
    Tensor nullSpace = LeftNullSpace.of(levers);
    return NormalizeTotal.FUNCTION.apply(target.dot(PseudoInverse.of(nullSpace)).dot(nullSpace));
  }
}

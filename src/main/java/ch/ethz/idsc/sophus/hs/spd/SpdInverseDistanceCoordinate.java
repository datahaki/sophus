// code by jph
package ch.ethz.idsc.sophus.hs.spd;

import ch.ethz.idsc.sophus.math.NormalizeTotal;
import ch.ethz.idsc.sophus.math.win.BarycentricCoordinate;
import ch.ethz.idsc.sophus.math.win.InverseDistanceWeighting;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.Flatten;
import ch.ethz.idsc.tensor.mat.LeftNullSpace;
import ch.ethz.idsc.tensor.mat.PseudoInverse;

public enum SpdInverseDistanceCoordinate implements BarycentricCoordinate {
  INSTANCE;

  private static final BarycentricCoordinate INVERSE_DISTANCE_WEIGHTING = //
      InverseDistanceWeighting.of(SpdMetric.INSTANCE);

  @Override // from InverseDistanceCoordinates
  public Tensor weights(Tensor sequence, Tensor point) {
    Tensor target = INVERSE_DISTANCE_WEIGHTING.weights(sequence, point);
    Tensor levers = Tensor.of(sequence.stream().map(new SpdExp(point)::log).map(Flatten::of));
    Tensor nullSpace = LeftNullSpace.of(levers);
    return NormalizeTotal.FUNCTION.apply(target.dot(PseudoInverse.of(nullSpace)).dot(nullSpace));
  }
}

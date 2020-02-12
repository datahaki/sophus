// code by jph
package ch.ethz.idsc.sophus.hs.spd;

import ch.ethz.idsc.sophus.hs.sn.SnMean;
import ch.ethz.idsc.sophus.math.NormalizeTotal;
import ch.ethz.idsc.sophus.math.win.InverseDistanceWeighting;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.Flatten;
import ch.ethz.idsc.tensor.mat.LeftNullSpace;
import ch.ethz.idsc.tensor.mat.PseudoInverse;

public enum SpdInverseDistanceCoordinates {
  ;
  private static final InverseDistanceWeighting INVERSE_DISTANCE_WEIGHTING = //
      new InverseDistanceWeighting(SpdDistance.INSTANCE);

  /** @param sequence
   * @param point
   * @return vector of affine weights
   * @see SnMean */
  public static Tensor of(Tensor sequence, Tensor point) {
    Tensor target = INVERSE_DISTANCE_WEIGHTING.of(sequence).apply(point);
    Tensor levers = Tensor.of(sequence.stream().map(new SpdExp(point)::log).map(Flatten::of));
    Tensor nullSpace = LeftNullSpace.of(levers);
    return NormalizeTotal.FUNCTION.apply(target.dot(PseudoInverse.of(nullSpace)).dot(nullSpace));
  }
}

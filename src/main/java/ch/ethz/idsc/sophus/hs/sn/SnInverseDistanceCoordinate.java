// code by jph
package ch.ethz.idsc.sophus.hs.sn;

import ch.ethz.idsc.sophus.math.NormalizeAffine;
import ch.ethz.idsc.sophus.math.win.BarycentricCoordinate;
import ch.ethz.idsc.sophus.math.win.InverseDistanceWeighting;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.mat.LeftNullSpace;
import ch.ethz.idsc.tensor.mat.PseudoInverse;

/** On S^n the inverse distance coordinate is determined explicitly, whereas
 * the weighted average is approximated iteratively.
 * 
 * Given a scattered sequence of sufficiently distributed points and a point on S^n,
 * then SnInverseDistanceCoordinate.weights(sequence, point) gives a weight vector
 * that satisfies SnMean(sequence, weights) == point.
 * 
 * @see SnMean */
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
  public Tensor weights(Tensor sequence, Tensor point) {
    Tensor target = barycentricCoordinate.weights(sequence, point);
    Tensor levers = Tensor.of(sequence.stream().map(new SnExp(point)::log));
    Tensor nullSpace = LeftNullSpace.of(levers);
    return NormalizeAffine.of(target, PseudoInverse.of(nullSpace), nullSpace);
  }
}

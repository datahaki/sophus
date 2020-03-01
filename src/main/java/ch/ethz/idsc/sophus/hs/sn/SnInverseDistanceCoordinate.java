// code by jph
package ch.ethz.idsc.sophus.hs.sn;

import ch.ethz.idsc.sophus.hs.HsBarycentricCoordinate;
import ch.ethz.idsc.sophus.lie.FlattenLog;
import ch.ethz.idsc.sophus.lie.rn.RnNorm;
import ch.ethz.idsc.sophus.lie.rn.RnNormSquared;
import ch.ethz.idsc.sophus.math.win.BarycentricCoordinate;
import ch.ethz.idsc.sophus.math.win.InverseNorm;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;

/** On S^n the inverse distance coordinate is determined explicitly, whereas
 * the weighted average is approximated iteratively.
 * 
 * Given a scattered sequence of sufficiently distributed points and a point on S^n,
 * then SnInverseDistanceCoordinate.weights(sequence, point) gives a weight vector
 * that satisfies SnMean(sequence, weights) == point.
 * 
 * @see SnMean */
public class SnInverseDistanceCoordinate extends HsBarycentricCoordinate {
  public static final BarycentricCoordinate INSTANCE = //
      new SnInverseDistanceCoordinate(InverseNorm.of(RnNorm.INSTANCE));
  public static final BarycentricCoordinate SQUARED = //
      new SnInverseDistanceCoordinate(InverseNorm.of(RnNormSquared.INSTANCE));

  /***************************************************/
  private SnInverseDistanceCoordinate(TensorUnaryOperator target) {
    super(target);
  }

  @Override
  public FlattenLog logAt(Tensor point) {
    return new SnExp(point);
  }
}

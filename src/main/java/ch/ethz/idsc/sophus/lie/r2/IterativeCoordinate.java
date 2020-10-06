// code by jph
package ch.ethz.idsc.sophus.lie.r2;

import java.io.Serializable;
import java.util.Objects;

import ch.ethz.idsc.sophus.gbc.ZeroCoordinate;
import ch.ethz.idsc.sophus.lie.rn.RnGeodesic;
import ch.ethz.idsc.sophus.math.NormalizeTotal;
import ch.ethz.idsc.sophus.ref.d1.CurveSubdivision;
import ch.ethz.idsc.tensor.Integers;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.RotateLeft;
import ch.ethz.idsc.tensor.red.Norm;
import ch.ethz.idsc.tensor.sca.InvertUnlessZero;

/** for k == 0 the coordinates are identical to three-point coordinates with mean value as barycenter
 * 
 * mean value coordinates are C^\infty and work for non-convex polygons
 * 
 * Reference:
 * "Iterative coordinates"
 * by Chongyang Deng, Qingjun Chang, Kai Hormann, 2020 */
public class IterativeCoordinate implements ZeroCoordinate, Serializable {
  private static final CurveSubdivision MIDPOINTS = ControlMidpoints.of(RnGeodesic.INSTANCE);

  /** @param tensorUnaryOperator
   * @param k
   * @return */
  public static ZeroCoordinate of(ZeroCoordinate tensorUnaryOperator, int k) {
    return new IterativeCoordinate(tensorUnaryOperator, k);
  }

  /** @param k non-negative
   * @return */
  public static ZeroCoordinate usingMeanValue(int k) {
    return k == 0 //
        ? ThreePointCoordinate.of(Barycenter.MEAN_VALUE)
        : new IterativeCoordinate(ThreePointWeighting.of(Barycenter.MEAN_VALUE), k);
  }

  /***************************************************/
  private final ZeroCoordinate tensorUnaryOperator;
  private final int k;

  /** @param k non-negative */
  /* package */ IterativeCoordinate(ZeroCoordinate tensorUnaryOperator, int k) {
    this.tensorUnaryOperator = Objects.requireNonNull(tensorUnaryOperator);
    this.k = Integers.requirePositiveOrZero(k);
  }

  @Override // from TensorUnaryOperator
  public Tensor fromLevers(Tensor levers) {
    Tensor scaling = inverseNorms(levers);
    return NormalizeTotal.FUNCTION.apply(scaling.pmul(recur(0, scaling.pmul(levers))));
  }

  /** @param depth
   * @param normalized points on circle
   * @return */
  private Tensor recur(int depth, Tensor normalized) {
    if (depth < k) {
      Tensor midpoints = MIDPOINTS.cyclic(normalized);
      Tensor scaling = inverseNorms(midpoints);
      return RotateLeft.of(MIDPOINTS.cyclic(scaling.pmul(recur(depth + 1, scaling.pmul(midpoints)))), -1);
    }
    return tensorUnaryOperator.fromLevers(normalized);
  }

  /** @param levers
   * @return */
  private static Tensor inverseNorms(Tensor levers) {
    return Tensor.of(levers.stream().map(Norm._2::ofVector).map(InvertUnlessZero.FUNCTION));
  }
}

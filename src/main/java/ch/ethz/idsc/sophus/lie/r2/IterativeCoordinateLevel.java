// code by jph
package ch.ethz.idsc.sophus.lie.r2;

import java.util.Objects;

import ch.ethz.idsc.sophus.gbc.ZeroCoordinate;
import ch.ethz.idsc.sophus.lie.rn.RnGeodesic;
import ch.ethz.idsc.sophus.ref.d1.CurveSubdivision;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.opt.TensorScalarFunction;
import ch.ethz.idsc.tensor.red.Norm;
import ch.ethz.idsc.tensor.sca.InvertUnlessZero;
import ch.ethz.idsc.tensor.sca.Sign;

/** for k == 0 the coordinates are identical to three-point coordinates with mean value as barycenter
 * 
 * mean value coordinates are C^\infty and work for non-convex polygons
 * 
 * Reference:
 * "Iterative coordinates"
 * by Chongyang Deng, Qingjun Chang, Kai Hormann, 2020 */
public class IterativeCoordinateLevel implements TensorScalarFunction {
  private static final long serialVersionUID = 2763677963395774512L;
  private static final CurveSubdivision MIDPOINTS = ControlMidpoints.of(RnGeodesic.INSTANCE);
  private static final int MAX_ITERATIONS = 16;

  /** @param tensorUnaryOperator
   * @return */
  public static TensorScalarFunction of(ZeroCoordinate tensorUnaryOperator) {
    return new IterativeCoordinateLevel(tensorUnaryOperator);
  }

  /** @return */
  public static TensorScalarFunction usingMeanValue() {
    return new IterativeCoordinateLevel(ThreePointWeighting.of(Barycenter.MEAN_VALUE));
  }

  /***************************************************/
  private final ZeroCoordinate tensorUnaryOperator;

  /** @param k non-negative */
  /* package */ IterativeCoordinateLevel(ZeroCoordinate tensorUnaryOperator) {
    this.tensorUnaryOperator = Objects.requireNonNull(tensorUnaryOperator);
  }

  @Override
  public Scalar apply(Tensor levers) {
    Tensor scaling = inverseNorms(levers);
    return RealScalar.of(recur(0, scaling.pmul(levers)));
  }

  /** @param depth
   * @param normalized points on circle
   * @return */
  private int recur(int depth, Tensor normalized) {
    Tensor weights = tensorUnaryOperator.fromLevers(normalized);
    if (weights.stream().map(Scalar.class::cast).allMatch(Sign::isPositiveOrZero))
      return depth;
    if (depth < MAX_ITERATIONS) {
      Tensor midpoints = MIDPOINTS.cyclic(normalized);
      return recur(depth + 1, inverseNorms(midpoints).pmul(midpoints));
    }
    return MAX_ITERATIONS;
  }

  /** @param levers
   * @return */
  private static Tensor inverseNorms(Tensor levers) {
    return Tensor.of(levers.stream().map(Norm._2::ofVector).map(InvertUnlessZero.FUNCTION));
  }
}

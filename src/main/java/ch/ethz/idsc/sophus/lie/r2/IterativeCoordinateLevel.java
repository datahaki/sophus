// code by jph
package ch.ethz.idsc.sophus.lie.r2;

import java.util.Objects;

import ch.ethz.idsc.sophus.gbc.Genesis;
import ch.ethz.idsc.sophus.lie.rn.RnGeodesic;
import ch.ethz.idsc.sophus.ref.d1.CurveSubdivision;
import ch.ethz.idsc.tensor.DoubleScalar;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.api.TensorScalarFunction;
import ch.ethz.idsc.tensor.sca.Chop;
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

  /** @param genesis
   * @return */
  public static TensorScalarFunction of(Genesis genesis, Chop chop, int max) {
    return new IterativeCoordinateLevel(genesis, chop, max);
  }

  /** @return */
  public static TensorScalarFunction usingMeanValue(Chop chop, int max) {
    return new IterativeCoordinateLevel(ThreePointCoordinate.of(Barycenter.MEAN_VALUE), chop, max);
  }

  /***************************************************/
  private final Genesis genesis;
  private final Chop chop;
  private final int max;

  /** @param k non-negative */
  /* package */ IterativeCoordinateLevel(Genesis genesis, Chop chop, int max) {
    this.genesis = Objects.requireNonNull(genesis);
    this.chop = chop;
    this.max = max;
  }

  @Override
  public Scalar apply(Tensor levers) {
    if (Polygons.isInside(levers)) {
      Tensor scaling = InverseNorm.INSTANCE.origin(levers);
      Tensor normalized = scaling.pmul(levers);
      int depth = 0;
      while (depth < max) {
        Tensor weights = genesis.origin(normalized);
        if (weights.stream().map(Scalar.class::cast).map(chop).allMatch(Sign::isPositiveOrZero))
          return RealScalar.of(depth);
        Tensor midpoints = MIDPOINTS.cyclic(normalized);
        normalized = InverseNorm.INSTANCE.origin(midpoints).pmul(midpoints);
        ++depth;
      }
      return RealScalar.of(depth);
    }
    return DoubleScalar.INDETERMINATE;
  }
}

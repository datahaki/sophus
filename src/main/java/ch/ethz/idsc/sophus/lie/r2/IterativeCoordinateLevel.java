// code by jph
package ch.ethz.idsc.sophus.lie.r2;

import java.util.Objects;
import java.util.OptionalInt;

import ch.ethz.idsc.sophus.math.Genesis;
import ch.ethz.idsc.tensor.DoubleScalar;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.api.TensorScalarFunction;
import ch.ethz.idsc.tensor.nrm.NormalizeTotal;
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
  /** @param genesis
   * @param chop
   * @param max
   * @return */
  public static TensorScalarFunction of(Genesis genesis, Chop chop, int max) {
    return new IterativeCoordinateLevel( //
        Objects.requireNonNull(genesis), //
        Objects.requireNonNull(chop), //
        max);
  }

  /***************************************************/
  private final Genesis genesis;
  private final Chop chop;
  private final int max;

  private IterativeCoordinateLevel(Genesis genesis, Chop chop, int max) {
    this.genesis = genesis;
    this.chop = chop;
    this.max = max;
  }

  @Override
  public Scalar apply(Tensor levers) {
    if (Polygons.isInside(levers)) {
      Tensor scaling = InverseNorm.INSTANCE.origin(levers);
      OptionalInt optionalInt = NormalizeTotal.indeterminate(scaling);
      if (!optionalInt.isPresent()) {
        Tensor normalized = scaling.pmul(levers);
        int depth = 0;
        while (depth < max) {
          Tensor weights = genesis.origin(normalized);
          if (weights.stream().map(Scalar.class::cast).map(chop).allMatch(Sign::isPositiveOrZero))
            return RealScalar.of(depth);
          Tensor midpoints = Adds.forward(normalized);
          normalized = InverseNorm.INSTANCE.origin(midpoints).pmul(midpoints);
          ++depth;
        }
        return RealScalar.of(depth);
      }
    }
    return DoubleScalar.INDETERMINATE;
  }
}

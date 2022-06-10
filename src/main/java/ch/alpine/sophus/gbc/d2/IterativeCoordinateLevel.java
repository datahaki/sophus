// code by jph
package ch.alpine.sophus.gbc.d2;

import java.util.Objects;
import java.util.OptionalInt;

import ch.alpine.sophus.crv.d2.OriginEnclosureQ;
import ch.alpine.sophus.hs.Genesis;
import ch.alpine.tensor.DoubleScalar;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.TensorScalarFunction;
import ch.alpine.tensor.nrm.NormalizeTotal;
import ch.alpine.tensor.red.Times;
import ch.alpine.tensor.sca.Chop;
import ch.alpine.tensor.sca.Sign;

/** for k == 0 the coordinates are identical to three-point coordinates with mean value as barycenter
 * 
 * mean value coordinates are C^\infty and work for non-convex polygons
 * 
 * Reference:
 * "Iterative coordinates"
 * by Chongyang Deng, Qingjun Chang, Kai Hormann, 2020 */
public record IterativeCoordinateLevel(Genesis genesis, Chop chop, int max) implements TensorScalarFunction {
  public IterativeCoordinateLevel {
    Objects.requireNonNull(genesis);
    Objects.requireNonNull(chop);
  }

  @Override
  public Scalar apply(Tensor levers) {
    if (OriginEnclosureQ.INSTANCE.test(levers)) {
      Tensor scaling = InverseNorm.INSTANCE.origin(levers);
      OptionalInt optionalInt = NormalizeTotal.indeterminate(scaling);
      if (!optionalInt.isPresent()) {
        Tensor normalized = Times.of(scaling, levers);
        int depth = 0;
        while (depth < max) {
          Tensor weights = genesis.origin(normalized);
          if (weights.stream().map(Scalar.class::cast).map(chop).allMatch(Sign::isPositiveOrZero))
            return RealScalar.of(depth);
          Tensor midpoints = Adds.forward(normalized);
          normalized = Times.of(InverseNorm.INSTANCE.origin(midpoints), midpoints);
          ++depth;
        }
        return RealScalar.of(depth);
      }
    }
    return DoubleScalar.INDETERMINATE;
  }
}

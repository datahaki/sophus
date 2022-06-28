// code by jph
package ch.alpine.sophus.gbc.d2;

import java.util.Objects;

import ch.alpine.sophus.crv.d2.OriginEnclosureQ;
import ch.alpine.sophus.hs.Genesis;
import ch.alpine.tensor.DoubleScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.ConstantArray;

/** @see InsidePolygonCoordinate
 * 
 * @param genesis that evaluates polygon coordinates at zero (0, 0) */
public record InsideConvexHullCoordinate(Genesis genesis) implements Genesis {
  public InsideConvexHullCoordinate {
    Objects.requireNonNull(genesis);
  }

  @Override // from BarycentricCoordinate
  public Tensor origin(Tensor levers) {
    return OriginEnclosureQ.isInsideConvexHull(levers) //
        ? genesis.origin(levers)
        : ConstantArray.of(DoubleScalar.INDETERMINATE, levers.length());
  }
}

// code by jph
package ch.alpine.sophus.gbc.d2;

import java.io.Serializable;
import java.util.Objects;

import ch.alpine.sophus.math.Genesis;
import ch.alpine.sophus.math.d2.Polygons;
import ch.alpine.tensor.DoubleScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.ConstantArray;

/** @see InsidePolygonCoordinate */
public class InsideConvexHullCoordinate implements Genesis, Serializable {
  /** @param genesis
   * @return */
  public static Genesis of(Genesis genesis) {
    return new InsideConvexHullCoordinate(Objects.requireNonNull(genesis));
  }

  // ---
  private final Genesis genesis;

  /** @param genesis that evaluates polygon coordinates at zero (0, 0) */
  private InsideConvexHullCoordinate(Genesis genesis) {
    this.genesis = genesis;
  }

  @Override // from BarycentricCoordinate
  public Tensor origin(Tensor levers) {
    return Polygons.isInsideConvexHull(levers) //
        ? genesis.origin(levers)
        : ConstantArray.of(DoubleScalar.INDETERMINATE, levers.length());
  }
}

// code by jph
package ch.ethz.idsc.sophus.lie.r2;

import java.io.Serializable;
import java.util.Objects;

import ch.ethz.idsc.sophus.gbc.Genesis;
import ch.ethz.idsc.tensor.DoubleScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.ConstantArray;

/** @see InsidePolygonCoordinate */
public class InsideConvexHullCoordinate implements Genesis, Serializable {
  /** @param genesis
   * @return */
  public static Genesis of(Genesis genesis) {
    return new InsideConvexHullCoordinate(Objects.requireNonNull(genesis));
  }

  /***************************************************/
  private final Genesis genesis;

  /** @param genesis that evaluates polygon coordinates at zero (0, 0) */
  private InsideConvexHullCoordinate(Genesis genesis) {
    this.genesis = genesis;
  }

  @Override // from BarycentricCoordinate
  public Tensor origin(Tensor levers) {
    return ConvexHull.isInside(levers) //
        ? genesis.origin(levers)
        : ConstantArray.of(DoubleScalar.INDETERMINATE, levers.length());
  }
}

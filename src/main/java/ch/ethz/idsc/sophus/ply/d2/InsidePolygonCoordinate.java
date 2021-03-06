// code by jph
package ch.ethz.idsc.sophus.ply.d2;

import java.io.Serializable;
import java.util.Objects;

import ch.ethz.idsc.sophus.math.Genesis;
import ch.ethz.idsc.tensor.DoubleScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.ConstantArray;

/** @see InsideConvexHullCoordinate */
public class InsidePolygonCoordinate implements Genesis, Serializable {
  /** @param genesis
   * @return */
  public static Genesis of(Genesis genesis) {
    return new InsidePolygonCoordinate(Objects.requireNonNull(genesis));
  }

  /***************************************************/
  private final Genesis genesis;

  /** @param genesis that evaluates polygon coordinates at zero (0, 0) */
  private InsidePolygonCoordinate(Genesis genesis) {
    this.genesis = genesis;
  }

  @Override // from BarycentricCoordinate
  public Tensor origin(Tensor levers) {
    return Polygons.isInside(levers) //
        ? genesis.origin(levers)
        : ConstantArray.of(DoubleScalar.INDETERMINATE, levers.length());
  }
}

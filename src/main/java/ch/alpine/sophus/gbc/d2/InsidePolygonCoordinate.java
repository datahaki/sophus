// code by jph
package ch.alpine.sophus.gbc.d2;

import java.io.Serializable;
import java.util.Objects;

import ch.alpine.sophus.crv.d2.OriginEnclosureQ;
import ch.alpine.sophus.math.Genesis;
import ch.alpine.tensor.DoubleScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.ConstantArray;

/** @see InsideConvexHullCoordinate */
public class InsidePolygonCoordinate implements Genesis, Serializable {
  /** @param genesis
   * @return */
  public static Genesis of(Genesis genesis) {
    return new InsidePolygonCoordinate(Objects.requireNonNull(genesis));
  }

  // ---
  private final Genesis genesis;

  /** @param genesis that evaluates polygon coordinates at zero (0, 0) */
  private InsidePolygonCoordinate(Genesis genesis) {
    this.genesis = genesis;
  }

  @Override // from BarycentricCoordinate
  public Tensor origin(Tensor levers) {
    return OriginEnclosureQ.INSTANCE.test(levers) //
        ? genesis.origin(levers)
        : ConstantArray.of(DoubleScalar.INDETERMINATE, levers.length());
  }
}

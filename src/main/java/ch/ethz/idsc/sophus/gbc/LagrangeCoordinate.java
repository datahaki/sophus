// code by jph
package ch.ethz.idsc.sophus.gbc;

import java.io.Serializable;
import java.util.Objects;
import java.util.stream.Stream;

import ch.ethz.idsc.sophus.math.NormalizeTotal;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.ConstantArray;
import ch.ethz.idsc.tensor.alg.Transpose;
import ch.ethz.idsc.tensor.alg.UnitVector;
import ch.ethz.idsc.tensor.itp.Fit;
import ch.ethz.idsc.tensor.mat.RowReduce;
import ch.ethz.idsc.tensor.sca.Chop;

/** attempts to produce positive weights for levers with zero in convex hull
 * 
 * Technique of using Lagrange multipliers inspired by the following reference:
 * "Polygon Laplacian Made Simple"
 * by Astrid Bunge, Philipp Herholz, Misha Kazhdan, Mario Botsch, 2020 */
public class LagrangeCoordinate implements Genesis, Serializable {
  /** @param genesis for instance InverseDistanceWeighting.of(InversePowerVariogram.of(2))
   * @return */
  public static Genesis of(Genesis genesis) {
    return new LagrangeCoordinate(Objects.requireNonNull(genesis));
  }

  /***************************************************/
  private final Genesis genesis;

  private LagrangeCoordinate(Genesis genesis) {
    this.genesis = genesis;
  }

  @Override // from Genesis
  public Tensor origin(Tensor levers) {
    /* normalize total for improved numerics */
    return NormalizeTotal.FUNCTION.apply(fit(genesis.origin(levers), levers));
  }

  /** @param target
   * @param levers
   * @return */
  public static Tensor fit2(Tensor target, Tensor levers) {
    Tensor rows = Transpose.of(levers).append(ConstantArray.of(RealScalar.ONE, levers.length()));
    int rank = rows.length();
    return Fit.lagrange(target, rows, UnitVector.of(rank, rank - 1));
  }

  /** Hint: implementation is cautious and removes redundant columns from levers
   * 
   * @param target
   * @param levers
   * @return */
  public static Tensor fit(Tensor target, Tensor levers) {
    Tensor reduce = RowReduce.of(Transpose.of(levers));
    Tensor rows = Tensor.of(Stream.concat( //
        reduce.stream().filter(vector -> !Chop._08.allZero(vector)), //
        Stream.of(ConstantArray.of(RealScalar.ONE, levers.length()))));
    // System.out.println(Dimensions.of(levers) + " " + Dimensions.of(reduce));
    int rank = rows.length();
    return Fit.lagrange(target, rows, UnitVector.of(rank, rank - 1));
  }
}

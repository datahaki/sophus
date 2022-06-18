// code by jph
package ch.alpine.sophus.dv;

import java.util.Objects;

import ch.alpine.sophus.hs.Genesis;
import ch.alpine.sophus.hs.HsGenesis;
import ch.alpine.sophus.hs.Manifold;
import ch.alpine.sophus.hs.Sedarim;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.ScalarUnaryOperator;
import ch.alpine.tensor.mat.gr.Mahalanobis;
import ch.alpine.tensor.nrm.NormalizeTotal;

/** bi-invariant
 * does not result in a symmetric distance matrix -> should not use for kriging
 * 
 * leverages distances are biinvariant
 * 
 * <p>computes form at given point based on points in sequence and returns
 * vector of evaluations dMah_x(p_i) of points in sequence.
 * 
 * <p>one evaluation of the leverages involves the computation of
 * <pre>
 * PseudoInverse[levers^T . levers]
 * </pre>
 * 
 * <p>References:
 * "Biinvariant Generalized Barycentric Coordinates on Lie Groups"
 * by Jan Hakenberg, 2020
 * 
 * "Biinvariant Distance Vectors"
 * by Jan Hakenberg, 2020 */
/* package */ class LeveragesBiinvariant extends BiinvariantBase implements Genesis {
  public LeveragesBiinvariant(Manifold manifold) {
    super(manifold);
  }

  @Override // from Biinvariant
  public Sedarim distances(Tensor sequence) {
    return HsGenesis.wrap(hsDesign(), this, sequence);
  }

  @Override // from Biinvariant
  public Sedarim coordinate(ScalarUnaryOperator variogram, Tensor sequence) {
    return HsGenesis.wrap(hsDesign(), new LeveragesGenesis(variogram), sequence);
  }

  @Override // from Biinvariant
  public Sedarim lagrainate(ScalarUnaryOperator variogram, Tensor sequence) {
    Objects.requireNonNull(variogram);
    Objects.requireNonNull(sequence);
    return point -> {
      Tensor levers = hsDesign().matrix(sequence, point);
      Tensor target = NormalizeTotal.FUNCTION.apply(origin(levers).map(variogram));
      return LagrangeCoordinates.of(levers, target);
    };
  }

  @Override // from Genesis
  public Tensor origin(Tensor levers) {
    return new Mahalanobis(levers).leverages_sqrt();
  }
}

// code by jph
package ch.ethz.idsc.sophus.math.win;

import ch.ethz.idsc.sophus.hs.r2.Se2CoveringParametricDistance;
import ch.ethz.idsc.sophus.lie.se2c.Se2CoveringBarycenter;
import ch.ethz.idsc.sophus.lie.se2c.Se2CoveringGroupElement;
import ch.ethz.idsc.sophus.math.NormalizeTotal;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.mat.PseudoInverse;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;

/** given sequence and mean the implementation computes the weights that satisfy
 * 
 * Se2CoveringBiinvariantMean[sequence, weights] == mean */
public class Se2CoveringAffineCoordinates implements TensorUnaryOperator {
  private static final InverseDistanceFromOrigin INVERSE_DISTANCE_FROM_ORIGIN = //
      new InverseDistanceFromOrigin(Se2CoveringParametricDistance.INSTANCE);
  // ---
  private final Tensor sequence;

  /** @param sequence */
  public Se2CoveringAffineCoordinates(Tensor sequence) {
    this.sequence = sequence;
  }

  @Override
  public Tensor apply(Tensor x) {
    Tensor matrix = Tensor.of(sequence.stream() //
        .map(new Se2CoveringGroupElement(x).inverse()::combine));
    Tensor nuls = NullSpaces.of(Se2CoveringBarycenter.equation(matrix));
    Tensor weights = INVERSE_DISTANCE_FROM_ORIGIN.apply(matrix);
    Tensor fit = weights.dot(PseudoInverse.of(nuls));
    return NormalizeTotal.FUNCTION.apply(fit.dot(nuls));
  }
}

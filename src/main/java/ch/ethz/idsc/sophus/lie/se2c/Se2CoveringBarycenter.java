// code by jph
package ch.ethz.idsc.sophus.lie.se2c;

import java.util.Objects;

import ch.ethz.idsc.sophus.lie.se2.Se2Skew;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.Transpose;
import ch.ethz.idsc.tensor.alg.UnitVector;
import ch.ethz.idsc.tensor.mat.LinearSolve;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;

/** given sequence of length 4 and mean the implementation computes
 * the unique weights that satisfy
 * 
 * Se2CoveringBiinvariantMean[sequence, weights] == mean
 * 
 * for sequences of length != 4 the nullspace of the matrix covers
 * possible weights (still subject to normalization) */
// TODO most functionality here should be merged with other code
public class Se2CoveringBarycenter implements TensorUnaryOperator {
  private static final Tensor RHS = UnitVector.of(4, 3);

  public static Tensor equation(Tensor xya) {
    return Se2Skew.of(xya, RealScalar.ONE).rhs().append(xya.Get(2)); // append biinvariant mean of angles
  }

  // ---
  private final Tensor sequence;

  /** @param sequence of length 4 */
  public Se2CoveringBarycenter(Tensor sequence) {
    this.sequence = Objects.requireNonNull(sequence);
  }

  @Override
  public Tensor apply(Tensor mean) {
    Tensor m4x4 = Tensor.of(sequence.stream() //
        .map(new Se2CoveringGroupElement(mean).inverse()::combine) //
        .map(Se2CoveringBarycenter::equation) //
        .map(row -> row.append(RealScalar.ONE)));
    return LinearSolve.of(Transpose.of(m4x4), RHS);
  }
}

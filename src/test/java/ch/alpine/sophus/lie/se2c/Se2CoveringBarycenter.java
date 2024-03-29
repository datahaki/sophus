// code by jph
package ch.alpine.sophus.lie.se2c;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Transpose;
import ch.alpine.tensor.alg.UnitVector;
import ch.alpine.tensor.api.TensorUnaryOperator;
import ch.alpine.tensor.ext.Integers;
import ch.alpine.tensor.mat.re.LinearSolve;

/** Remark:
 * Functionality is superseded by Se2CoveringInverseDistanceCoordinates
 * 
 * Se2CoveringBarycenter remains only for testing purpose.
 * 
 * given sequence of length 4 and mean the implementation computes
 * the unique weights that satisfy
 * 
 * Se2CoveringBiinvariantMean[sequence, weights] == mean
 * 
 * for sequences of length != 4 the nullspace of the matrix covers
 * possible weights (still subject to normalization) */
/* package */ class Se2CoveringBarycenter implements TensorUnaryOperator {
  private static final Tensor RHS = UnitVector.of(4, 3);
  // ---
  private final Tensor sequence;

  /** @param sequence of length 4 */
  public Se2CoveringBarycenter(Tensor sequence) {
    Integers.requireEquals(sequence.length(), 4);
    this.sequence = sequence;
  }

  @Override
  public Tensor apply(Tensor mean) {
    Tensor m4x4 = Tensor.of(sequence.stream() //
        .map(new Se2CoveringGroupElement(mean).inverse()::combine) //
        .map(Se2CoveringBarycenter::equation) //
        .map(row -> row.append(RealScalar.ONE)));
    return LinearSolve.of(Transpose.of(m4x4), RHS);
  }

  public static Tensor equation(Tensor xya) {
    return Se2Skew.of(xya, RealScalar.ONE).rhs().append(xya.Get(2)); // append biinvariant mean of angles
  }
}

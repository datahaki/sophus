// code by jph
package ch.ethz.idsc.sophus.math.win;

import java.util.Objects;

import ch.ethz.idsc.sophus.math.NormalizeTotal;
import ch.ethz.idsc.sophus.math.TensorNorm;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.mat.PseudoInverse;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;

/** Inverse Distance Coordinates are generalized barycentric coordinates with the properties
 * partition of unity
 * linear reproduction
 * Lagrange
 * C^infinity (except at points from input set)
 * 
 * in general, the coordinates may evaluate to be negative
 * 
 * Reference:
 * "Inverse Distance Coordinates for Scattered Sets of Points"
 * by Hakenberg, 2020 */
public class InverseDistanceCoordinates implements TensorUnaryOperator {
  /** @param tensorNorm for instance Norm._2::ofVector
   * @param sequence matrix of dimensions n x d
   * @return */
  public static TensorUnaryOperator of(TensorNorm tensorNorm, Tensor sequence) {
    return new InverseDistanceCoordinates( //
        InverseNorm.of(tensorNorm), //
        Objects.requireNonNull(sequence));
  }

  private final TensorUnaryOperator operator;
  private final Tensor sequence;

  private InverseDistanceCoordinates(TensorUnaryOperator operator, Tensor sequence) {
    this.operator = operator;
    this.sequence = sequence;
  }

  @Override
  public Tensor apply(Tensor x) {
    Tensor levers = Tensor.of(sequence.stream().map(x.negate()::add));
    Tensor nullSpace = NullSpaces.of(levers);
    Tensor target = operator.apply(levers);
    return NormalizeTotal.FUNCTION.apply(target.dot(PseudoInverse.of(nullSpace)).dot(nullSpace));
  }
}

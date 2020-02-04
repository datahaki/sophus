// code by jph
package ch.ethz.idsc.sophus.math.win;

import ch.ethz.idsc.sophus.math.NormalizeTotal;
import ch.ethz.idsc.sophus.math.TensorNorm;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.mat.PseudoInverse;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;

public class RnNullCoordinates implements TensorUnaryOperator {
  public static TensorUnaryOperator of(TensorNorm tensorNorm, Tensor sequence) {
    return new RnNullCoordinates(tensorNorm, sequence);
  }

  private final InverseDistanceFromOrigin inverseDistanceFromOrigin;
  private final Tensor sequence;

  private RnNullCoordinates(TensorNorm tensorNorm, Tensor sequence) {
    inverseDistanceFromOrigin = new InverseDistanceFromOrigin(tensorNorm);
    this.sequence = sequence;
  }

  @Override
  public Tensor apply(Tensor x) {
    Tensor matrix = Tensor.of(sequence.stream().map(x.negate()::add));
    Tensor nuls = NullSpaces.of(matrix);
    Tensor weights = inverseDistanceFromOrigin.apply(matrix);
    Tensor fit = weights.dot(PseudoInverse.of(nuls));
    return NormalizeTotal.FUNCTION.apply(fit.dot(nuls));
  }
}

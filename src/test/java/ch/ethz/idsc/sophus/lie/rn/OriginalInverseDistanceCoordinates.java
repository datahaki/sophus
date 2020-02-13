// code by jph
package ch.ethz.idsc.sophus.lie.rn;

import ch.ethz.idsc.sophus.math.NormalizeTotal;
import ch.ethz.idsc.sophus.math.win.InverseDistanceCoordinates;
import ch.ethz.idsc.sophus.math.win.InverseNorm;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.mat.LeftNullSpace;
import ch.ethz.idsc.tensor.mat.PseudoInverse;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;

/* package */ enum OriginalInverseDistanceCoordinates implements InverseDistanceCoordinates {
  INSTANCE;

  private static final TensorUnaryOperator INVERSE_NORM = InverseNorm.of(RnNorm.INSTANCE);

  @Override // from InverseDistanceCoordinates
  public Tensor weights(Tensor sequence, Tensor x) {
    Tensor levers = Tensor.of(sequence.stream().map(x.negate()::add));
    Tensor nullSpace = LeftNullSpace.of(levers);
    Tensor target = INVERSE_NORM.apply(levers);
    return NormalizeTotal.FUNCTION.apply(target.dot(PseudoInverse.of(nullSpace)).dot(nullSpace));
  }
}

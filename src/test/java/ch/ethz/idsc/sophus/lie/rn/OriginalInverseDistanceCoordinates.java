// code by jph
package ch.ethz.idsc.sophus.lie.rn;

import ch.ethz.idsc.sophus.gbc.BarycentricCoordinate;
import ch.ethz.idsc.sophus.math.NormalizeTotal;
import ch.ethz.idsc.sophus.math.id.InverseNorm;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.mat.LeftNullSpace;
import ch.ethz.idsc.tensor.mat.PseudoInverse;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;

/* package */ enum OriginalInverseDistanceCoordinates implements BarycentricCoordinate {
  INSTANCE;

  private static final TensorUnaryOperator INVERSE_NORM = InverseNorm.of(RnNorm.INSTANCE);

  @Override // from BarycentricCoordinate
  public Tensor weights(Tensor sequence, Tensor x) {
    Tensor levers = Tensor.of(sequence.stream().map(x.negate()::add));
    Tensor nullSpace = LeftNullSpace.of(levers);
    Tensor target = INVERSE_NORM.apply(levers);
    return NormalizeTotal.FUNCTION.apply(target.dot(PseudoInverse.of(nullSpace)).dot(nullSpace));
  }
}

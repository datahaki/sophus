// code by jph
package ch.ethz.idsc.sophus.math.win;

import java.io.Serializable;
import java.util.Objects;

import ch.ethz.idsc.sophus.lie.LieGroup;
import ch.ethz.idsc.sophus.lie.LieGroupElement;
import ch.ethz.idsc.sophus.math.NormalizeAffine;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.Join;
import ch.ethz.idsc.tensor.mat.LeftNullSpace;
import ch.ethz.idsc.tensor.mat.PseudoInverse;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;

public class BiinvariantInverseDistanceCoordinate implements BarycentricCoordinate, Serializable {
  private final LieGroup lieGroup;
  private final TensorUnaryOperator log;
  private final TensorUnaryOperator inv_norm;

  /** @param lieGroup
   * @param log
   * @param neutral
   * @param inv_norm */
  // TODO design is not good: neutral element should depend on group element
  public BiinvariantInverseDistanceCoordinate(LieGroup lieGroup, TensorUnaryOperator log, TensorUnaryOperator inv_norm) {
    this.lieGroup = Objects.requireNonNull(lieGroup);
    this.log = Objects.requireNonNull(log);
    this.inv_norm = Objects.requireNonNull(inv_norm);
  }

  @Override // from BarycentricCoordinate
  public Tensor weights(Tensor sequence, Tensor point) {
    LieGroupElement element = lieGroup.element(point);
    Tensor levers1 = Tensor.of(sequence.stream() //
        .map(element.inverse()::combine) //
        .map(log));
    Tensor levers2 = Tensor.of(sequence.stream() //
        .map(lieGroup::element) //
        .map(LieGroupElement::inverse) //
        .map(LieGroupElement::toCoordinate) //
        .map(element::combine) //
        .map(log));
    Tensor levers = Join.of(1, levers1, levers2);
    Tensor nullsp = LeftNullSpace.of(levers);
    Tensor target = inv_norm.apply(levers);
    return NormalizeAffine.of(target, PseudoInverse.of(nullsp), nullsp);
  }
}

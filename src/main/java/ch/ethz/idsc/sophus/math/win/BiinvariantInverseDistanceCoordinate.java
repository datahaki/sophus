// code by jph
package ch.ethz.idsc.sophus.math.win;

import java.io.Serializable;
import java.util.Objects;

import ch.ethz.idsc.sophus.lie.LieGroup;
import ch.ethz.idsc.sophus.lie.LieGroupElement;
import ch.ethz.idsc.sophus.lie.he.HeAdNorm;
import ch.ethz.idsc.sophus.lie.rn.RnNorm;
import ch.ethz.idsc.sophus.math.NormalizeAffine;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.Join;
import ch.ethz.idsc.tensor.mat.LeftNullSpace;
import ch.ethz.idsc.tensor.mat.PseudoInverse;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;

/** Log[g.m.g^-1] == Ad[g].Log[m]
 * 
 * Log[g.m] == Ad[g].Log[m.g]
 * Log[g^-1.m] == Ad[g^-1].Log[m.g^-1]
 * Ad[g].Log[g^-1.m] == Log[m.g^-1] */
public class BiinvariantInverseDistanceCoordinate implements BarycentricCoordinate, Serializable {
  private final LieGroup lieGroup;
  private final TensorUnaryOperator log;
  private final TensorUnaryOperator inv_norm;
  private final InverseBiNorm inverseBiNorm;

  /** @param lieGroup
   * @param log
   * @param inv_norm */
  public BiinvariantInverseDistanceCoordinate( //
      LieGroup lieGroup, //
      TensorUnaryOperator log) {
    this.lieGroup = Objects.requireNonNull(lieGroup);
    this.log = Objects.requireNonNull(log);
    inv_norm = InverseNorm.of(HeAdNorm.INSTANCE);
    inverseBiNorm = InverseBiNorm.of(RnNorm.INSTANCE);
  }

  @Override // from BarycentricCoordinate
  public Tensor weights(Tensor sequence, Tensor point) {
    LieGroupElement lieGroupElement = lieGroup.element(point);
    Tensor levers1 = Tensor.of(sequence.stream() //
        .map(lieGroupElement.inverse()::combine) // invariance to left-action
        .map(log));
    Tensor levers2 = Tensor.of(sequence.stream() // invariance to right-action
        .map(lieGroup::element) //
        .map(LieGroupElement::inverse) //
        .map(LieGroupElement::toCoordinate) //
        .map(lieGroupElement::combine) //
        .map(log));
    Tensor levers = levers1; //
    levers = Join.of(1, levers1, levers2);
    Tensor nullsp = LeftNullSpace.of(levers);
    // Tensor target = inv_norm.apply(levers1);
    // Tensor right = Tensor.of(sequence.stream() // invariance to right-action
    // .map(lieGroup::element) //
    // .map(LieGroupElement::inverse) //
    // .map(LieGroupElement::toCoordinate) //
    // .map(lieGroupElement::combine) //
    // .map(HeExponential.INSTANCE::log));
    // Tensor rightL = Tensor.of(right.stream().map(lieGroupElement::adjoint).map(Flatten::of));
    Tensor target = inverseBiNorm.binorm(levers1, levers2);
    return NormalizeAffine.of(target, PseudoInverse.of(nullsp), nullsp);
  }
}

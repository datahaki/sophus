// code by jph
package ch.ethz.idsc.sophus.math.win;

import java.io.Serializable;
import java.util.Objects;

import ch.ethz.idsc.sophus.lie.LieGroup;
import ch.ethz.idsc.sophus.math.NormalizeAffine;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.mat.LeftNullSpace;
import ch.ethz.idsc.tensor.mat.PseudoInverse;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;

/** invariance under left-action is guaranteed because
 * log [(g x)^-1 g p] == log [x^-1 p]
 * 
 * if the norm is Ad invariant then invariance under right action is guaranteed */
public class LieInverseDistanceCoordinate implements BarycentricCoordinate, Serializable {
  private final LieGroup lieGroup;
  private final TensorUnaryOperator equation;
  private final TensorUnaryOperator inv_norm;

  /** @param lieGroup
   * @param equation mapping from group to vector space
   * @param inv_norm
   * @throws Exception if any input parameter is null */
  public LieInverseDistanceCoordinate(LieGroup lieGroup, TensorUnaryOperator equation, TensorUnaryOperator inv_norm) {
    this.lieGroup = Objects.requireNonNull(lieGroup);
    this.equation = Objects.requireNonNull(equation);
    this.inv_norm = Objects.requireNonNull(inv_norm);
  }

  @Override // from BarycentricCoordinate
  public Tensor weights(Tensor sequence, Tensor point) {
    Tensor levers = Tensor.of(sequence.stream() //
        .map(lieGroup.element(point).inverse()::combine) //
        .map(equation));
    // System.out.println("levers=" + Pretty.of(levers.map(Round._4)));
    Tensor nullsp = LeftNullSpace.of(levers);
    Tensor target = inv_norm.apply(levers);
    // System.out.println("nullsp=" + Pretty.of(nullsp.map(Round._4)));
    // System.out.println("target=" + Pretty.of(target.map(Round._4)));
    return NormalizeAffine.of(target, PseudoInverse.of(nullsp), nullsp);
  }
}

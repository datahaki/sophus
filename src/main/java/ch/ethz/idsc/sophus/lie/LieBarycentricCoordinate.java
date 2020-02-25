// code by jph
package ch.ethz.idsc.sophus.lie;

import java.io.Serializable;
import java.util.Objects;

import ch.ethz.idsc.sophus.math.NormalizeAffine;
import ch.ethz.idsc.sophus.math.win.BarycentricCoordinate;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.mat.LeftNullSpace;
import ch.ethz.idsc.tensor.mat.PseudoInverse;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;

/** invariance under left-action is guaranteed because
 * log [(g x)^-1 g p] == log [x^-1 p]
 * 
 * If the target mapping is Ad invariant then invariance under right action
 * and inversion is guaranteed.
 * 
 * If the target mapping correlates to inverse distances then the coordinates
 * satisfy the Lagrange property. */
public class LieBarycentricCoordinate implements BarycentricCoordinate, Serializable {
  private final LieGroup lieGroup;
  private final TensorUnaryOperator log;
  private final TensorUnaryOperator target;

  /** @param lieGroup
   * @param log mapping from group to Lie algebra
   * @param target
   * @throws Exception if any input parameter is null */
  public LieBarycentricCoordinate( //
      LieGroup lieGroup, //
      TensorUnaryOperator log, //
      TensorUnaryOperator target) {
    this.lieGroup = Objects.requireNonNull(lieGroup);
    this.log = Objects.requireNonNull(log);
    this.target = Objects.requireNonNull(target);
  }

  @Override // from BarycentricCoordinate
  public final Tensor weights(Tensor sequence, Tensor point) {
    Tensor levers = Tensor.of(sequence.stream() //
        .map(lieGroup.element(point).inverse()::combine) //
        .map(log));
    Tensor nullsp = LeftNullSpace.of(levers);
    return NormalizeAffine.of( //
        // target.apply(projection(sequence, point).subtract(IdentityMatrix.of(sequence.length()))), //
        target.apply(levers), //
        PseudoInverse.of(nullsp), nullsp);
  }

  /** function for testing
   * 
   * @param sequence
   * @param point
   * @return */
  public Tensor projection(Tensor sequence, Tensor point) {
    Tensor levers = Tensor.of(sequence.stream() //
        .map(lieGroup.element(point).inverse()::combine) //
        .map(log));
    Tensor nullsp = LeftNullSpace.of(levers);
    return PseudoInverse.of(nullsp).dot(nullsp);
  }
}

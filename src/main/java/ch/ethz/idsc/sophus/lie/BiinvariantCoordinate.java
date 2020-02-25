package ch.ethz.idsc.sophus.lie;

import java.io.Serializable;

import ch.ethz.idsc.sophus.math.NormalizeAffine;
import ch.ethz.idsc.sophus.math.win.BarycentricCoordinate;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.mat.IdentityMatrix;
import ch.ethz.idsc.tensor.mat.LeftNullSpace;
import ch.ethz.idsc.tensor.mat.PseudoInverse;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;

public class BiinvariantCoordinate implements BarycentricCoordinate, Serializable {
  private final LieGroup lieGroup;
  private final TensorUnaryOperator log;
  private final TensorUnaryOperator target;

  /** @param lieGroup
   * @param log mapping from group to Lie algebra
   * @param target
   * @throws Exception if any input parameter is null */
  public BiinvariantCoordinate( //
      LieGroup lieGroup, //
      TensorUnaryOperator log, //
      TensorUnaryOperator target) {
    this.lieGroup = lieGroup;
    this.log = log;
    this.target = target;
  }

  @Override // from BarycentricCoordinate
  public Tensor weights(Tensor sequence, Tensor point) {
    Tensor projection = projection(sequence, point);
    return NormalizeAffine.of( //
        target.apply(projection.subtract(IdentityMatrix.of(sequence.length()))), //
        projection);
  }

  public Tensor projection(Tensor sequence, Tensor point) {
    Tensor levers = Tensor.of(sequence.stream() //
        .map(lieGroup.element(point).inverse()::combine) //
        .map(log));
    Tensor nullsp = LeftNullSpace.of(levers);
    return PseudoInverse.of(nullsp).dot(nullsp);
  }
}

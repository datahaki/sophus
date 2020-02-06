// code by jph
package ch.ethz.idsc.sophus.math.win;

import java.io.Serializable;
import java.util.Objects;

import ch.ethz.idsc.sophus.lie.LieGroup;
import ch.ethz.idsc.sophus.math.NormalizeTotal;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.mat.PseudoInverse;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;

public class LieInverseDistanceCoordinates implements Serializable {
  private final LieGroup lieGroup;
  private final TensorUnaryOperator equation;
  private final TensorUnaryOperator inv_norm;

  /** @param lieGroup
   * @param equation
   * @param inv_norm
   * @throws Exception if any input parameter is null */
  public LieInverseDistanceCoordinates(LieGroup lieGroup, TensorUnaryOperator equation, TensorUnaryOperator inv_norm) {
    this.lieGroup = Objects.requireNonNull(lieGroup);
    this.equation = Objects.requireNonNull(equation);
    this.inv_norm = Objects.requireNonNull(inv_norm);
  }

  /** @param sequence of coordinates in Lie group
   * @return */
  public TensorUnaryOperator of(Tensor sequence) {
    return new Operator(Objects.requireNonNull(sequence));
  }

  private class Operator implements TensorUnaryOperator {
    private final Tensor sequence;

    public Operator(Tensor sequence) {
      this.sequence = sequence;
    }

    @Override
    public Tensor apply(Tensor x) {
      Tensor levers = Tensor.of(sequence.stream().map(lieGroup.element(x).inverse()::combine).map(equation));
      Tensor nullSpace = NullSpaces.of(levers);
      return NormalizeTotal.FUNCTION.apply(inv_norm.apply(levers).dot(PseudoInverse.of(nullSpace)).dot(nullSpace));
    }
  }
}

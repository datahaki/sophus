// code by jph
package ch.ethz.idsc.sophus.math.win;

import java.io.Serializable;
import java.util.Objects;

import ch.ethz.idsc.sophus.lie.LieGroup;
import ch.ethz.idsc.sophus.math.NormalizeTotal;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.mat.PseudoInverse;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;

public class LieIdfCoordinates implements Serializable {
  private final LieGroup lieGroup;
  private final TensorUnaryOperator equation;
  private final TensorUnaryOperator inv_dist;

  /** @param lieGroup
   * @param equation
   * @param inv_dist
   * @throws Exception if any input parameter is null */
  public LieIdfCoordinates(LieGroup lieGroup, TensorUnaryOperator equation, TensorUnaryOperator inv_dist) {
    this.lieGroup = Objects.requireNonNull(lieGroup);
    this.equation = Objects.requireNonNull(equation);
    this.inv_dist = Objects.requireNonNull(inv_dist);
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
      return NormalizeTotal.FUNCTION.apply(inv_dist.apply(levers).dot(PseudoInverse.of(nullSpace)).dot(nullSpace));
    }
  }
}

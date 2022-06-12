// code by jph
package ch.alpine.sophus.decim;

import java.io.Serializable;

import ch.alpine.sophus.api.TensorNorm;
import ch.alpine.sophus.lie.LieGroup;
import ch.alpine.sophus.lie.LieGroupElement;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.TensorUnaryOperator;
import ch.alpine.tensor.nrm.NormalizeUnlessZero;
import ch.alpine.tensor.nrm.Vector2Norm;
import ch.alpine.tensor.red.Times;

// TODO SOPHUS at least discuss
public record LieProjectedLineDistance(LieGroup lieGroup) implements LineDistance, Serializable {
  private static final TensorUnaryOperator NORMALIZE_UNLESS_ZERO = NormalizeUnlessZero.with(Vector2Norm::of);

  @Override // from LineDistance
  public TensorNorm tensorNorm(Tensor beg, Tensor end) {
    return new NormImpl(beg, end);
  }

  private class NormImpl implements TensorNorm, Serializable {
    private final LieGroupElement lieBeg;
    private final LieGroupElement lieInv;
    private final Tensor normal;

    public NormImpl(Tensor beg, Tensor end) {
      lieBeg = lieGroup.element(beg);
      lieInv = lieBeg.inverse();
      this.normal = NORMALIZE_UNLESS_ZERO.apply(lieGroup.log(lieInv.combine(end)));
    }

    @Override // from TensorNorm
    public Scalar norm(Tensor tensor) {
      Tensor vector = lieGroup.log(lieInv.combine(tensor)); // tensor - p
      Tensor project = Times.of(vector.dot(normal), normal);
      Tensor along = lieBeg.combine(lieGroup.exp(project));
      Tensor dir = lieGroup.element(along).inverse().combine(tensor);
      return Vector2Norm.of(lieGroup.log(dir));
    }
  }
}

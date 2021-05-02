// code by jph
package ch.alpine.sophus.decim;

import java.io.Serializable;
import java.util.Objects;

import ch.alpine.sophus.lie.LieGroup;
import ch.alpine.sophus.lie.LieGroupElement;
import ch.alpine.sophus.math.Exponential;
import ch.alpine.sophus.math.TensorNorm;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.TensorUnaryOperator;
import ch.alpine.tensor.nrm.NormalizeUnlessZero;
import ch.alpine.tensor.nrm.Vector2Norm;

public class LieProjectedLineDistance implements LineDistance, Serializable {
  private static final TensorUnaryOperator NORMALIZE_UNLESS_ZERO = NormalizeUnlessZero.with(Vector2Norm::of);
  // ---
  private final LieGroup lieGroup;
  private final Exponential exponential;

  public LieProjectedLineDistance(LieGroup lieGroup, Exponential exponential) {
    this.lieGroup = Objects.requireNonNull(lieGroup);
    this.exponential = Objects.requireNonNull(exponential);
  }

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
      this.normal = NORMALIZE_UNLESS_ZERO.apply(exponential.log(lieInv.combine(end)));
    }

    @Override // from TensorNorm
    public Scalar norm(Tensor tensor) {
      Tensor vector = exponential.log(lieInv.combine(tensor)); // tensor - p
      Tensor project = vector.dot(normal).pmul(normal);
      Tensor along = lieBeg.combine(exponential.exp(project));
      Tensor dir = lieGroup.element(along).inverse().combine(tensor);
      return Vector2Norm.of(exponential.log(dir));
    }
  }
}